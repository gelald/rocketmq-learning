package com.github.gelald.rocketmq.producer.starter.producer;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Component
public class RocketMQProducer {
    private RocketMQTemplate rocketMQTemplate;

    public SendResult sendDuplexMessage() {
        SendResult sendResult = this.rocketMQTemplate.syncSend("RocketMQStarterOrdinaryTopic:duplex", "two-way message");
        return sendResult;
    }

    public void sendSimplexMessage() {
        this.rocketMQTemplate.sendOneWay("RocketMQStarterOrdinaryTopic:simplex", "one-way message");
    }

    @Autowired
    public void setRocketMQTemplate(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }
}
