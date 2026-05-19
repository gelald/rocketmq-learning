package com.github.gelald.rocketmq.consumer.starter.consumer.filter;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
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
        filterExpressionType = "sql92",
        tag = "(TAGS is not null and TAGS = 'phone') and (price between 400 and 500)"
)
@ConditionalOnProperty(prefix = "learning.rocketmq.consumer.consumer-switch", name = "filter", havingValue = "true")
public class SQLFilterConsumer implements RocketMQListener {
    @Override
    public ConsumeResult consume(MessageView messageView) {
        String topic = messageView.getTopic();
        String tag = messageView.getTag().orElse("");
        ByteBuffer byteBuffer = messageView.getBody();
        String body = new String(new byte[byteBuffer.remaining()], StandardCharsets.UTF_8);
        log.info("SQLFilterConsumer接收消息, topic: {}, tag: {}, 消息内容: {}", topic, tag, body);
        return ConsumeResult.SUCCESS;
    }
}
