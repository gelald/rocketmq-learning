package com.github.gelald.rocketmq.producer.client.controller;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@RestController
@Api(tags = "普通消息生产者")
@RequestMapping("/mq-producer")
public class DefaultProducerController {

    private DefaultMQProducer defaultMQProducer;

    @ApiOperation("同步发送普通消息")
    @GetMapping("/sync-ordinary")
    public SendResult sendOrdinaryMessageSynchronously() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message((RocketMQConstant.TOPIC_PREFIX + "client"), "sync", "send ordinary message synchronously".getBytes(StandardCharsets.UTF_8));
        SendResult sendResult = this.defaultMQProducer.send(message);
        return sendResult;
    }

    @ApiOperation("异步发送普通消息")
    @GetMapping("/async-ordinary")
    public String sendOrdinaryMessageAsynchronously() throws RemotingException, InterruptedException, MQClientException {
        Message message = new Message((RocketMQConstant.TOPIC_PREFIX + "client"), "async", "send ordinary message asynchronously".getBytes(StandardCharsets.UTF_8));
        this.defaultMQProducer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("消息发送成功: {}", sendResult.toString());
            }

            @Override
            public void onException(Throwable e) {
                log.info("消息发送失败，原因: ", e);
            }
        });
        return "send complete";
    }

    @ApiOperation("发送单向普通消息")
    @GetMapping("/one-way")
    public String sendSimplexMessage() throws RemotingException, InterruptedException, MQClientException {
        Message message = new Message((RocketMQConstant.TOPIC_PREFIX + "client"), "one-way", "send one-way message".getBytes(StandardCharsets.UTF_8));
        this.defaultMQProducer.sendOneway(message);
        return "send complete";
    }

    @ApiOperation("测试集群消费模式")
    @GetMapping("/clustering-consume")
    public String clusteringConsume() throws RemotingException, InterruptedException, MQClientException, MQBrokerException {
        for (int i = 0; i < 20; i++) {
            String messageBody = "测试集群消费模式第" + (i + 1) + "条消息";
            Message message = new Message((RocketMQConstant.TOPIC_PREFIX + "client-clustering"), messageBody.getBytes(StandardCharsets.UTF_8));
            this.defaultMQProducer.send(message);
        }
        return "send complete";
    }

    @ApiOperation("测试广播消费模式")
    @GetMapping("/broadcast-consume")
    public String broadcastConsume() throws RemotingException, InterruptedException, MQClientException, MQBrokerException {
        for (int i = 0; i < 20; i++) {
            String messageBody = "测试广播消费模式第" + (i + 1) + "条消息";
            Message message = new Message((RocketMQConstant.TOPIC_PREFIX + "client-broadcast"), messageBody.getBytes(StandardCharsets.UTF_8));
            this.defaultMQProducer.send(message);
        }
        return "send complete";
    }

    @ApiOperation("发送延时消息")
    @GetMapping("/delay-message")
    public String sendDelayMessage() throws RemotingException, InterruptedException, MQClientException, MQBrokerException {
        Message message = new Message((RocketMQConstant.TOPIC_PREFIX + "client"), "delay", "send third delay level message".getBytes(StandardCharsets.UTF_8));
        message.setDelayTimeLevel(3);
        message.putUserProperty("delayTime", "10秒");
        this.defaultMQProducer.send(message);
        return "send complete";
    }

    @ApiOperation("批量发送消息")
    @GetMapping("/batch-message")
    public SendResult sendBatchMessage() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        List<Message> messages = new ArrayList<>(10);
        for (int i = 1; i <= 10; i++) {
            String messageBody = "测试批量发送消息第" + i + "条消息";
            Message message = new Message((RocketMQConstant.TOPIC_PREFIX + "client"), "batch", messageBody.getBytes(StandardCharsets.UTF_8));
            messages.add(message);
        }
        return this.defaultMQProducer.send(messages);
    }

    @Autowired
    public void setDefaultMQProducer(DefaultMQProducer defaultMQProducer) {
        this.defaultMQProducer = defaultMQProducer;
    }
}
