package com.github.gelald.rocketmq.consumer.starter.consumer.model;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author WuYingBin
 * date: 2022/8/23
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = (RocketMQConstant.CONSUMER_GROUP_PREFIX + "starter-clustering"),
        topic = (RocketMQConstant.TOPIC_PREFIX + "starter-clustering"),
        // 设置消费模式为集群消费
        messageModel = MessageModel.CLUSTERING
)
@ConditionalOnProperty(prefix = "learning.rocketmq.consumer.consumer-switch", name = "message-model", havingValue = "true")
public class ClusteringConsumerOne implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
    @Override
    public void onMessage(String message) {
        log.info("ClusteringConsumerOne接收到消息, 消息内容: {}", message);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.setInstanceName("clustering-consumer-one");
    }
}
