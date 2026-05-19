package com.github.gelald.rocketmq.consumer.starter.consumer;

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
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author WuYingBin
 * Date 2022/8/24
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = (RocketMQConstant.CONSUMER_GROUP_PREFIX + "starter-transaction"),
        topic = (RocketMQConstant.TOPIC_PREFIX + "starter-transaction")
)
@ConditionalOnProperty(prefix = "learning.rocketmq.consumer.consumer-switch", name = "transaction", havingValue = "true")
public class TransactionConsumer implements RocketMQListener {
    @Override
    public ConsumeResult consume(MessageView messageView) {
        log.info("接收到事务消息，准备执行消费者本地事务");
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

        log.info("事务消息内容, topic: {}, tags: {}, keys: {}, 消息内容: {}", topic, tag, keys, messageBody);
        try {
            log.info("本地事务执行中...");
            TimeUnit.MILLISECONDS.sleep(1000);
            log.info("本地事务执行成功");
            return ConsumeResult.SUCCESS;
        } catch (InterruptedException e) {
            log.info("本地事务执行失败，稍后重新消费消息或人工干预");
            return ConsumeResult.FAILURE;
        }
    }
}
