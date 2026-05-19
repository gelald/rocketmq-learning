package com.github.gelald.rocketmq.producer.starter.controller;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.apis.producer.Transaction;
import org.apache.rocketmq.client.common.Pair;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@Api(tags = "消息生产者")
@RestController
@RequestMapping("/mq-producer")
public class RocketMQProducerController {
    private RocketMQClientTemplate rocketMQClientTemplate;

    @ApiOperation("同步发送普通消息")
    @GetMapping("/sync-ordinary")
    public SendReceipt sendMessageSynchronously() {
        Message<String> message = MessageBuilder.withPayload("send ordinary message synchronously")
                .setHeader("keys", "sync-msg-key").build();
        log.info("生产者发送消息: {}", message);
        SendReceipt sendReceipt = this.rocketMQClientTemplate.syncSendNormalMessage((RocketMQConstant.TOPIC_PREFIX + "starter:sync"), message);
        log.info("消息发送状态: {}", sendReceipt);
        return sendReceipt;
    }

    @ApiOperation("异步发送普通消息")
    @GetMapping("/async-ordinary")
    public String sendMessageAsynchronously() {
        Message<String> message = MessageBuilder.withPayload("send ordinary message asynchronously").build();
        CompletableFuture<SendReceipt> future = new CompletableFuture<>();
        future.whenCompleteAsync((sendReceipt, throwable) -> {
            if (throwable != null) {
                log.error("[异步发送失败] key={} | 原因={}", sendReceipt.getMessageId(), throwable.getMessage(), throwable);
                return;
            }
            log.info("[异步发送成功] messageId={}", sendReceipt.getMessageId());
        }).orTimeout(5, java.util.concurrent.TimeUnit.SECONDS);
        this.rocketMQClientTemplate.asyncSendNormalMessage((RocketMQConstant.TOPIC_PREFIX + "starter:async"), message, future);
        log.info("生产者发送消息: {}", message);
        return "sent message";
    }

    @ApiOperation("测试全局有序消息")
    @GetMapping("/global-order")
    public String sendGlobalOrderMessage() {
        for (int i = 0; i < 10; i++) {
            String messageBody = "测试全局有序消息第" + (i + 1) + "条消息";
            Message<String> message = MessageBuilder.withPayload(messageBody).build();
            log.info("生产者发送消息: {}", message);
            // 传入messageGroup来指定具体的一个队列
            this.rocketMQClientTemplate.syncSendFifoMessage((RocketMQConstant.TOPIC_PREFIX + "starter-global-order"), message, "111");
        }
        return "sent message";
    }

    @ApiOperation("测试分区有序消息")
    @GetMapping("/partitioned-order")
    public String sendPartitionedOrderMessage() {
        Message<String> message1 = MessageBuilder.withPayload("订单1创建").build();
        log.info("生产者发送消息: {}", message1);
        this.rocketMQClientTemplate.syncSendFifoMessage((RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order"), message1, "111");
        Message<String> message2 = MessageBuilder.withPayload("订单2创建").build();
        log.info("生产者发送消息: {}", message2);
        this.rocketMQClientTemplate.syncSendFifoMessage((RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order"), message2, "222");
        Message<String> message3 = MessageBuilder.withPayload("订单1支付").build();
        log.info("生产者发送消息: {}", message3);
        this.rocketMQClientTemplate.syncSendFifoMessage((RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order"), message3, "111");
        Message<String> message4 = MessageBuilder.withPayload("订单2支付").build();
        log.info("生产者发送消息: {}", message4);
        this.rocketMQClientTemplate.syncSendFifoMessage((RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order"), message4, "222");
        Message<String> message5 = MessageBuilder.withPayload("订单1发货").build();
        log.info("生产者发送消息: {}", message5);
        this.rocketMQClientTemplate.syncSendFifoMessage((RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order"), message5, "111");
        Message<String> message6 = MessageBuilder.withPayload("订单2发货").build();
        log.info("生产者发送消息: {}", message6);
        this.rocketMQClientTemplate.syncSendFifoMessage((RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order"), message6, "222");
        return "sent message";
    }

    @ApiOperation("测试tag过滤消息")
    @GetMapping("/tag-filter-message")
    public String tagFilterMessage() {
        // 消费者方设置如下
        // 消费者只接受tag为phone或clothes的消息
        Message<String> message1 = MessageBuilder.withPayload("订单1").build();
        log.info("生产者发送消息: {}", message1);
        this.rocketMQClientTemplate.syncSendNormalMessage((RocketMQConstant.TOPIC_PREFIX + "starter-tag-filter:phone"), message1);
        Message<String> message2 = MessageBuilder.withPayload("订单2").build();
        log.info("生产者发送消息: {}", message2);
        this.rocketMQClientTemplate.syncSendNormalMessage((RocketMQConstant.TOPIC_PREFIX + "starter-tag-filter:shoes"), message2);
        Message<String> message3 = MessageBuilder.withPayload("订单3").build();
        log.info("生产者发送消息: {}", message3);
        this.rocketMQClientTemplate.syncSendNormalMessage((RocketMQConstant.TOPIC_PREFIX + "starter-tag-filter:clothes"), message3);
        return "sent message";
    }

    @ApiOperation("发送事务消息")
    @GetMapping("/transaction/{number}")
    public String sendTransactionMessage(@PathVariable Integer number) throws ClientException {
        log.info("接收到事务请求，准备执行生产者本地事务...");
        Message<String> message = MessageBuilder.withPayload("通知消费者执行本地事务的事务消息" + number).build();
        Pair<SendReceipt, Transaction> pair = this.rocketMQClientTemplate.sendTransactionMessage((RocketMQConstant.TOPIC_PREFIX + "starter-transaction"), message);
        SendReceipt sendReceipt = pair.getSendReceipt();
        Transaction transaction = pair.getTransaction();
        if (sendReceipt != null) {
            log.info("事务消息发送成功，提交事务，{}", sendReceipt.getMessageId());
            transaction.commit();
        } else {
            log.info("事务消息发送失败，回滚事务");
            transaction.rollback();
        }
        return "sent message";
    }
}
