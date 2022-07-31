package com.github.gelald.rocketmq.producer.client.controller;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@Api(tags = "消息生产者")
@RestController
@RequestMapping("/mq-producer")
public class RocketMQProducerController {

    private DefaultMQProducer defaultMQProducer;
    private TransactionMQProducer transactionMQProducer;

    @ApiOperation("发送带有响应的普通消息")
    @GetMapping("/duplex")
    public SendResult sendDuplexMessage() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message((RocketMQConstant.TOPIC_PREFIX + "client-ordinary"), "duplex", "two-way message".getBytes(StandardCharsets.UTF_8));
        SendResult sendResult = this.defaultMQProducer.send(message);
        return sendResult;
    }

    @ApiOperation("发送单向消息")
    @GetMapping("/simplex")
    public String sendSimplexMessage() throws RemotingException, InterruptedException, MQClientException {
        Message message = new Message((RocketMQConstant.TOPIC_PREFIX + "client-ordinary"), "simplex", "one-way message".getBytes(StandardCharsets.UTF_8));
        this.defaultMQProducer.sendOneway(message);
        return "send successfully";
    }

    @Autowired(required = false)
    public void setDefaultMQProducer(DefaultMQProducer defaultMQProducer) {
        this.defaultMQProducer = defaultMQProducer;
    }

    @Autowired(required = false)
    public void setTransactionMQProducer(TransactionMQProducer transactionMQProducer) {
        this.transactionMQProducer = transactionMQProducer;
    }
}
