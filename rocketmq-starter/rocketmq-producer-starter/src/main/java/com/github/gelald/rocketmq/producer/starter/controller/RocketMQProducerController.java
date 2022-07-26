package com.github.gelald.rocketmq.producer.starter.controller;

import com.github.gelald.rocketmq.producer.starter.producer.RocketMQProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@RestController
@RequestMapping("/mq-producer")
public class RocketMQProducerController {
    private RocketMQProducer rocketMQProducer;

    @GetMapping("/duplex")
    public SendResult sendDuplexMessage() {
        SendResult sendResult = this.rocketMQProducer.sendDuplexMessage();
        return sendResult;
    }

    @GetMapping("/simplex")
    public String sendSimplexMessage() {
        this.rocketMQProducer.sendSimplexMessage();
        return "send successfully";
    }

    @Autowired
    public void setRocketMQProducer(RocketMQProducer rocketMQProducer) {
        this.rocketMQProducer = rocketMQProducer;
    }
}
