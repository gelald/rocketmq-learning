package com.github.gelald.rocketmq.consumer.starter.consumer.order;

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
        consumerGroup = (RocketMQConstant.CONSUMER_GROUP_PREFIX + "starter-partitioned-order"),
        topic = (RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order")
)
@ConditionalOnProperty(prefix = "learning.rocketmq.consumer.consumer-switch", name = "order", havingValue = "true")
public class PartitionedConsumerOne implements RocketMQListener {
    @Override
    public ConsumeResult consume(MessageView messageView) {
        // 从 MessageView 中获取 ByteBuffer
        ByteBuffer byteBuffer = messageView.getBody();

        // 转换 ByteBuffer 为字节数组
        byte[] body = new byte[byteBuffer.remaining()];
        byteBuffer.get(body);
        // 处理字节数组，例如转换为字符串
        String messageBody = new String(body, StandardCharsets.UTF_8);

        log.info("PartitionedConsumerOne接收到消息, 消息内容: {}", messageBody);
        return ConsumeResult.SUCCESS;
    }
}
