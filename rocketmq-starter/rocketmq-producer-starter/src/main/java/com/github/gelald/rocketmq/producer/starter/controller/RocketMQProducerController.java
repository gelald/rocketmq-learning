package com.github.gelald.rocketmq.producer.starter.controller;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        log.info("消息发送成功: {}", sendResult);
        return sendResult;
    }

    @ApiOperation("异步发送普通消息")
    @GetMapping("/async-ordinary")
    public String sendMessageAsynchronously() {
        Message<String> message = MessageBuilder.withPayload("send ordinary message asynchronously").build();
        this.rocketMQTemplate.asyncSend((RocketMQConstant.TOPIC_PREFIX + "starter:async"), message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("消息发送成功: {}", sendResult);
            }

            @Override
            public void onException(Throwable e) {
                log.info("消息发送失败，原因: ", e);
            }
        });
        log.info("生产者发送消息: {}", message);
        return "send successfully";
    }

    @ApiOperation("发送单向普通消息")
    @GetMapping("/one-way")
    public String sendOneWayMessage() {
        Message<String> message = MessageBuilder.withPayload("send one-way message").build();
        log.info("生产者发送消息: {}", message);
        this.rocketMQTemplate.sendOneWay((RocketMQConstant.TOPIC_PREFIX + "starter:one-way"), message);
        return "send successfully";
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
        return "send successfully";
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
        return "send successfully";
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
        return "send successfully";
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
        return "send successfully";
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


    @Autowired
    public void setRocketMQTemplate(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }
}
