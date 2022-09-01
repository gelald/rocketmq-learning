package com.github.gelald.rocketmq.consumer.starter.consumer.order;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author WuYingBin
 * Date 2022/8/24
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = (RocketMQConstant.CONSUMER_GROUP_PREFIX + "starter-partitioned-order"),
        topic = (RocketMQConstant.TOPIC_PREFIX + "starter-partitioned-order"),
        consumeMode = ConsumeMode.ORDERLY
)
@ConditionalOnProperty(prefix = "learning.rocketmq.consumer.consumer-switch", name = "order", havingValue = "true")
public class PartitionedConsumerOne implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
    @Override
    public void onMessage(String message) {
        log.info("PartitionedConsumerOne接收到消息, 消息内容: {}", message);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.setInstanceName("partitioned-order-consumer-one");
    }
}
