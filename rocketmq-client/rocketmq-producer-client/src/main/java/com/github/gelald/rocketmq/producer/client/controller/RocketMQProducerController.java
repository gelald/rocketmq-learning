package com.github.gelald.rocketmq.producer.client.controller;

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
@RestController
@RequestMapping("/mq-producer")
public class RocketMQProducerController {

    private DefaultMQProducer defaultMQProducer;
    private TransactionMQProducer transactionMQProducer;

    @GetMapping("/duplex")
    public SendResult sendDuplexMessage() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message("RocketMQClientOrdinaryTopic", "duplex", "two-way message".getBytes(StandardCharsets.UTF_8));
        SendResult sendResult = this.defaultMQProducer.send(message);
        return sendResult;
    }

    @GetMapping("/simplex")
    public String sendSimplexMessage() throws RemotingException, InterruptedException, MQClientException {
        Message message = new Message("RocketMQClientOrdinaryTopic", "simplex", "one-way message".getBytes(StandardCharsets.UTF_8));
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
