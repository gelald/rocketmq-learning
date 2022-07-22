package com.github.gelald.rocketmq.stream.controller;

import com.github.gelald.rocketmq.stream.producer.RocketMQStreamProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WuYingBin
 * Date 2022/7/22
 */
@Slf4j
@RestController
@RequestMapping("/mq-producer")
public class RocketMQStreamController {
    private RocketMQStreamProducer rocketMQStreamProducer;

    @GetMapping("/sendString")
    public Object send(String msg) {
        if (!StringUtils.hasText(msg)) {
            return "success";
        }
        log.info("发送消息到MQ: {}", msg);
        boolean b = this.rocketMQStreamProducer.sendMessage(msg);
        if (!b) {
            log.info("消息发送失败");
            return "fail";
        }
        log.info("消息发送成功");
        return "success";
    }

    @Autowired
    public void setRocketMQStreamProducer(RocketMQStreamProducer rocketMQStreamProducer) {
        this.rocketMQStreamProducer = rocketMQStreamProducer;
    }
}
