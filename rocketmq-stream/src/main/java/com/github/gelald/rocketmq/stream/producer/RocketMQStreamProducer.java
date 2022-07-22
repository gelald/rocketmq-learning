package com.github.gelald.rocketmq.stream.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WuYingBin
 * Date 2022/7/22
 */
@Slf4j
@Component
public class RocketMQStreamProducer {

    private Source source;

    public boolean sendMessage(String messageBody) {
        Map<String, Object> map = new HashMap<>();
        map.put(MessageConst.PROPERTY_TAGS, "TestStreamTag");
        MessageHeaders messageHeaders = new MessageHeaders(map);
        Message<String> message = MessageBuilder.createMessage(messageBody, messageHeaders);
        return this.source.output().send(message);
    }

    @Autowired
    public void setSource(Source source) {
        this.source = source;
    }
}
