package com.github.gelald.rocketmq.producer.client.controller;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.RecallReceipt;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.java.message.MessageBuilderImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@RestController
@Api(tags = "普通消息生产者")
@RequestMapping("/mq-producer")
@AllArgsConstructor
public class DefaultProducerController {

    private Producer defaultMQProducer;

    @ApiOperation("同步发送普通消息")
    @GetMapping("/sync-ordinary")
    public SendReceipt sendOrdinaryMessageSynchronously() throws ClientException {
        Message message = new MessageBuilderImpl()
                .setTopic((RocketMQConstant.TOPIC_PREFIX + "client"))
                .setTag("sync")
                .setBody("send ordinary message synchronously".getBytes(StandardCharsets.UTF_8))
                .build();
        SendReceipt sendReceipt = this.defaultMQProducer.send(message);
        log.info("消息发送状态: {}", sendReceipt);
        return sendReceipt;
    }

    @ApiOperation("异步发送普通消息")
    @GetMapping("/async-ordinary")
    public String sendOrdinaryMessageAsynchronously() {
        Message message = new MessageBuilderImpl()
                .setTopic((RocketMQConstant.TOPIC_PREFIX + "client"))
                .setTag("async")
                .setBody("send ordinary message asynchronously".getBytes(StandardCharsets.UTF_8))
                .build();
        CompletableFuture<SendReceipt> future = this.defaultMQProducer.sendAsync(message);
        future.whenComplete((sendReceipt, throwable) -> {
            if (throwable != null) {
                log.error("消息发送失败: {}", throwable.getMessage());
                return;
            }
            log.info("消息发送状态: {}", sendReceipt);
        });
        return "send complete";
    }

    @ApiOperation("撤回未消费消息")
    @GetMapping("/recall")
    public String recallMessage() throws ClientException, InterruptedException {
        Message message = new MessageBuilderImpl()
                .setTopic((RocketMQConstant.TOPIC_PREFIX + "client"))
                .setTag("recall")
                .setBody("send ordinary message synchronously, and then recall it".getBytes(StandardCharsets.UTF_8))
                .build();
        SendReceipt sendReceipt = this.defaultMQProducer.send(message);
        log.info("消息发送状态: {}", sendReceipt);
        TimeUnit.MILLISECONDS.sleep(1500);
        String messageId = sendReceipt.getMessageId().toString();
        String recallHandle = sendReceipt.getRecallHandle();
        log.info("消息: {} 需要被撤回, recallHandle: {}", messageId, recallHandle);
        RecallReceipt recallReceipt = this.defaultMQProducer.recallMessage((RocketMQConstant.TOPIC_PREFIX + "client"), recallHandle);
        log.info("消息撤回状态: {}", recallReceipt);
        return "recall complete";
    }

    @ApiOperation("发送延时消息")
    @GetMapping("/delay-message")
    public String sendDelayMessage() throws ClientException {
        Message message = new MessageBuilderImpl()
                .setTopic((RocketMQConstant.TOPIC_PREFIX + "client"))
                .setTag("delay")
                .setBody("send third delay message".getBytes(StandardCharsets.UTF_8))
                .setDeliveryTimestamp(LocalDateTime.now().plusSeconds(10).toEpochSecond(ZoneOffset.UTC))
                .build();
        this.defaultMQProducer.send(message);
        return "send complete";
    }
}
