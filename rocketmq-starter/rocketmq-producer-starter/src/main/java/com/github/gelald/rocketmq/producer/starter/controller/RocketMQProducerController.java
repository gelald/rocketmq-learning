package com.github.gelald.rocketmq.producer.starter.controller;

import com.github.gelald.rocketmq.producer.starter.producer.RocketMQProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "消息生产者")
@RestController
@RequestMapping("/mq-producer")
public class RocketMQProducerController {
    private RocketMQProducer rocketMQProducer;

    @ApiOperation("发送有返回值的消息")
    @GetMapping("/duplex")
    public SendResult sendDuplexMessage() {
        SendResult sendResult = this.rocketMQProducer.sendDuplexMessage();
        return sendResult;
    }

    @ApiOperation("发送单向消息")
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
