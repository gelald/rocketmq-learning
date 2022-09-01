package com.github.gelald.rocketmq.consumer.starter.consumer.filter;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author WuYingBin
 * Date 2022/8/24
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = (RocketMQConstant.CONSUMER_GROUP_PREFIX + "starter-sql-filter"),
        topic = (RocketMQConstant.TOPIC_PREFIX + "starter-sql-filter"),
        selectorType = SelectorType.SQL92,
        selectorExpression = "(TAGS is not null and TAGS = 'phone') and (price between 400 and 500)"
)
@ConditionalOnProperty(prefix = "learning.rocketmq.consumer.consumer-switch", name = "filter", havingValue = "true")
public class SQLFilterConsumer implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        String topic = messageExt.getTopic();
        String tags = messageExt.getTags();
        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("TagFilterConsumer接收消息, topic: {}, tags: {}, 消息内容: {}", topic, tags, body);
    }
}
