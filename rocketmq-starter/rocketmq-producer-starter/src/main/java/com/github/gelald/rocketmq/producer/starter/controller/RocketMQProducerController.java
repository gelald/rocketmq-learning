package com.github.gelald.rocketmq.producer.starter.controller;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@Api(tags = "消息生产者")
@RestController
@RequestMapping("/mq-producer")
public class RocketMQProducerController {
    private RocketMQTemplate rocketMQTemplate;

    @ApiOperation("同步发送普通消息")
    @GetMapping("/sync-ordinary")
    public SendResult sendMessageSynchronously() {
        Message<String> message = MessageBuilder.withPayload("send ordinary message synchronously").build();
        log.info("生产者发送消息: {}", message);
        SendResult sendResult = this.rocketMQTemplate.syncSend((RocketMQConstant.TOPIC_PREFIX + "starter:sync"), message);
        log.info("消息发送状态: {}", sendResult);
        return sendResult;
    }

    @ApiOperation("异步发送普通消息")
    @GetMapping("/async-ordinary")
    public String sendMessageAsynchronously() {
        Message<String> message = MessageBuilder.withPayload("send ordinary message asynchronously").build();
        this.rocketMQTemplate.asyncSend((RocketMQConstant.TOPIC_PREFIX + "starter:async"), message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("消息发送状态: {}", sendResult);
            }

            @Override
            public void onException(Throwable e) {
                log.info("消息发送失败，原因: ", e);
            }
        });
        log.info("生产者发送消息: {}", message);
        return "sent message";
    }

    @ApiOperation("发送单向普通消息")
    @GetMapping("/one-way")
    public String sendOneWayMessage() {
        Message<String> message = MessageBuilder.withPayload("send one-way message").build();
        log.info("生产者发送消息: {}", message);
        this.rocketMQTemplate.sendOneWay((RocketMQConstant.TOPIC_PREFIX + "starter:one-way"), message);
        return "sent message";
    }

    @ApiOperation("测试集群消费模式")
    @GetMapping("/clustering-consume")
    public String clusteringConsume() {
        for (int i = 0; i < 20; i++) {
            String messageBody = "测试集群消费模式第" + (i + 1) + "条消息";
            Message<String> message = MessageBuilder.withPayload(messageBody).build();
            log.info("生产者发送消息: {}", message);
            this.rocketMQTemplate.syncSend((RocketMQConstant.TOPIC_PREFIX + "starter-clustering"), message);
        }
        return "sent message";
    }

    @ApiOperation("测试广播消费模式")
    @GetMapping("/broadcast-consume")
    public String broadcastConsume() {
        for (int i = 0; i < 20; i++) {
            String messageBody = "测试广播消费模式第" + (i + 1) + "条消息";
            Message<String> message = MessageBuilder.withPayload(messageBody).build();
            log.info("生产者发送消息: {}", message);
            this.rocketMQTemplate.syncSend((RocketMQConstant.TOPIC_PREFIX + "starter-broadcast"), message);
        }
        return "sent message";
    }

    @ApiOperation("测试全局有序消息")
    @GetMapping("/global-order")
    public String sendGlobalOrderMessage() {
        for (int i = 0; i < 10; i++) {
            String messageBody = "测试全局有序消息第" + (i + 1) + "条消息";
            Message<String> message = MessageBuilder.withPayload(messageBody).build();
            log.info("生产者发送消息: {}", message);
            this.rocketMQTemplate.sendOneWayOrderly((RocketMQConstant.TOPIC_PREFIX + "starter-global-order"), message, "123");
        }
        return "sent message";
    }

    @ApiOperation("测试分区有序消息")
    @GetMapping("/partitioned-order")
    public String sendPartitionedOrderMessage() {
        Message<String> message1 = MessageBuilder.withPayload("订单1创建").build();
        log.info("生产者发送消息: {}", message1);
        this.rocketMQTemplate.sendOneWayOrderly((RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order"), message1, "111");
        Message<String> message2 = MessageBuilder.withPayload("订单2创建").build();
        log.info("生产者发送消息: {}", message2);
        this.rocketMQTemplate.sendOneWayOrderly((RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order"), message2, "222");
        Message<String> message3 = MessageBuilder.withPayload("订单1支付").build();
        log.info("生产者发送消息: {}", message3);
        this.rocketMQTemplate.sendOneWayOrderly((RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order"), message3, "111");
        Message<String> message4 = MessageBuilder.withPayload("订单2支付").build();
        log.info("生产者发送消息: {}", message4);
        this.rocketMQTemplate.sendOneWayOrderly((RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order"), message4, "222");
        Message<String> message5 = MessageBuilder.withPayload("订单1发货").build();
        log.info("生产者发送消息: {}", message5);
        this.rocketMQTemplate.sendOneWayOrderly((RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order"), message5, "111");
        Message<String> message6 = MessageBuilder.withPayload("订单2发货").build();
        log.info("生产者发送消息: {}", message6);
        this.rocketMQTemplate.sendOneWayOrderly((RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order"), message6, "222");
        return "sent message";
    }

    @ApiOperation("发送延时消息")
    @GetMapping("/delay")
    public SendResult sendDelayMessage() {
        Message<String> message = MessageBuilder.withPayload("send delay message").build();
        log.info("生产者发送消息: {}", message);
        SendResult sendResult = this.rocketMQTemplate.syncSend((RocketMQConstant.TOPIC_PREFIX + "starter:delay"), message, 3000, 2);
        log.info("消息发送状态: {}", sendResult);
        return sendResult;
    }

    @ApiOperation("批量发送消息")
    @GetMapping("/batch")
    public SendResult sendMessageInBatch() {
        List<Message<String>> messages = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String messageBody = "批量发送消息第" + i + "条";
            Message<String> message = MessageBuilder.withPayload(messageBody).build();
            messages.add(message);
            log.info("生产者发送消息: {}", message);
        }
        SendResult sendResult = this.rocketMQTemplate.syncSend((RocketMQConstant.TOPIC_PREFIX + "starter:batch"), messages);
        log.info("消息发送状态: {}", sendResult);
        return sendResult;
    }

    @ApiOperation("测试tag过滤消息")
    @GetMapping("/tag-filter-message")
    public String tagFilterMessage() {
        // 消费者方设置如下
        // 消费者只接受tag为phone或clothes的消息
        Message<String> message1 = MessageBuilder.withPayload("订单1").build();
        log.info("生产者发送消息: {}", message1);
        this.rocketMQTemplate.sendOneWay((RocketMQConstant.TOPIC_PREFIX + "starter-tag-filter:phone"), message1);
        Message<String> message2 = MessageBuilder.withPayload("订单2").build();
        log.info("生产者发送消息: {}", message2);
        this.rocketMQTemplate.sendOneWay((RocketMQConstant.TOPIC_PREFIX + "starter-tag-filter:shoes"), message2);
        Message<String> message3 = MessageBuilder.withPayload("订单3").build();
        log.info("生产者发送消息: {}", message3);
        this.rocketMQTemplate.sendOneWay((RocketMQConstant.TOPIC_PREFIX + "starter-tag-filter:clothes"), message3);
        return "sent message";
    }

    @ApiOperation("测试sql过滤消息")
    @GetMapping("/sql-filter-message")
    public String sqlFilterMessage() {
        // 消费者方设置如下
        // 消费者只接受tag为phone而且price在[400-500]区间的消息
        Message<String> message1 = MessageBuilder.withPayload("订单1").setHeader("price", 600).build();
        log.info("生产者发送消息: {}", message1);
        this.rocketMQTemplate.sendOneWay((RocketMQConstant.TOPIC_PREFIX + "starter-sql-filter:phone"), message1);
        Message<String> message2 = MessageBuilder.withPayload("订单2").setHeader("price", 420).build();
        log.info("生产者发送消息: {}", message2);
        this.rocketMQTemplate.sendOneWay((RocketMQConstant.TOPIC_PREFIX + "starter-sql-filter:phone"), message1);
        Message<String> message3 = MessageBuilder.withPayload("订单3").setHeader("price", 480).build();
        log.info("生产者发送消息: {}", message3);
        this.rocketMQTemplate.sendOneWay((RocketMQConstant.TOPIC_PREFIX + "starter-sql-filter"), message1);
        Message<String> message4 = MessageBuilder.withPayload("订单4").setHeader("price", 500).build();
        log.info("生产者发送消息: {}", message4);
        this.rocketMQTemplate.sendOneWay((RocketMQConstant.TOPIC_PREFIX + "starter-sql-filter:phone"), message4);
        return "sent message";
    }

    @ApiOperation("发送事务消息")
    @GetMapping("/transaction/{number}")
    public TransactionSendResult sendTransactionMessage(@PathVariable Integer number) {
        log.info("接收到事务请求，准备执行生产者本地事务...");
        Message<String> message = MessageBuilder.withPayload("通知消费者执行本地事务的事务消息").build();
        TransactionSendResult transactionSendResult = this.rocketMQTemplate.sendMessageInTransaction((RocketMQConstant.TOPIC_PREFIX + "starter-transaction"), message, number);
        log.info("生产者发送状态: {}", transactionSendResult.getSendStatus().toString());
        log.info("本地事务执行结果: {}", transactionSendResult.getLocalTransactionState().toString());
        return transactionSendResult;
    }

    @Autowired
    public void setRocketMQTemplate(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }
}
