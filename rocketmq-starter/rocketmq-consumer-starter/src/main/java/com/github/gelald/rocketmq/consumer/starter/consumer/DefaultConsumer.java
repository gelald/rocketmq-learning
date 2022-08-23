package com.github.gelald.rocketmq.consumer.starter.consumer;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = (RocketMQConstant.CONSUMER_GROUP_PREFIX + "starter"),
        topic = (RocketMQConstant.TOPIC_PREFIX + "starter")
)
public class DefaultConsumer implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        String topic = messageExt.getTopic();
        String tags = messageExt.getTags();
        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("DefaultConsumer接收消息, topic: {}, tags: {}, 消息内容: {}", topic, tags, body);
    }
}
