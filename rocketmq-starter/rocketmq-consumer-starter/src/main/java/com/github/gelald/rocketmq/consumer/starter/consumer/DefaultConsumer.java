package com.github.gelald.rocketmq.consumer.starter.consumer;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

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
public class DefaultConsumer implements RocketMQListener {
    @Override
    public ConsumeResult consume(MessageView messageView) {
        String topic = messageView.getTopic();
        String tag = messageView.getTag().orElse("");
        Collection<String> keys = messageView.getKeys();

        // 从 MessageView 中获取 ByteBuffer
        ByteBuffer byteBuffer = messageView.getBody();

        // 转换 ByteBuffer 为字节数组
        byte[] body = new byte[byteBuffer.remaining()];
        byteBuffer.get(body);
        // 处理字节数组，例如转换为字符串
        String messageBody = new String(body, StandardCharsets.UTF_8);

        log.info("DefaultConsumer接收消息, topic: {}, tags: {}, keys: {}, 消息内容: {}", topic, tag, keys, messageBody);
        return ConsumeResult.SUCCESS;
    }
}
