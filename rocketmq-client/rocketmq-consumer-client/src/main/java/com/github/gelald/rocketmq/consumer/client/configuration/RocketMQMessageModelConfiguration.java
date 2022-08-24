package com.github.gelald.rocketmq.consumer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定义测试消费模式的消费者
 *
 * @author WuYingBin
 * date: 2022/8/19
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "learning.rocketmq.consumer.consumer-switch", name = "message-model", havingValue = "true")
public class RocketMQMessageModelConfiguration extends RocketMQBaseConsumerConfiguration {
    /**
     * 集群消费的消费者1
     */
    @Bean
    public DefaultMQPushConsumer clusteringMQPushConsumerOne(MessageListenerConcurrently clusteringListenerOne) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setInstanceName("clustering-consumer-one");
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-clustering"));
        // 设置消费模式，默认是集群消费模式
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-clustering"), "*");
        defaultMQPushConsumer.setMessageListener(clusteringListenerOne);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }

    /**
     * 集群消费的消费者2
     */
    @Bean
    public DefaultMQPushConsumer clusteringMQPushConsumerTwo(MessageListenerConcurrently clusteringListenerTwo) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(this.rocketMQConsumerProperties.getNameServerAddr());
        // todo org.apache.rocketmq.client.impl.consumer.DefaultMQPushConsumerImpl.start
        defaultMQPushConsumer.setInstanceName("clustering-consumer-two");
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-clustering"));
        // 设置消费模式，默认是集群消费模式
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-clustering"), "*");
        defaultMQPushConsumer.setMessageListener(clusteringListenerTwo);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }

    /**
     * 广播消费的消费者1
     */
    @Bean
    public DefaultMQPushConsumer broadcastMQPushConsumerOne(MessageListenerConcurrently broadcastListenerOne) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(this.rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setInstanceName("broadcast-consumer-one");
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-broadcast"));
        // 设置消费模式，默认是集群消费模式
        defaultMQPushConsumer.setMessageModel(MessageModel.BROADCASTING);
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-broadcast"), "*");
        defaultMQPushConsumer.setMessageListener(broadcastListenerOne);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }

    /**
     * 广播消费的消费者2
     */
    @Bean
    public DefaultMQPushConsumer broadcastMQPushConsumerTwo(MessageListenerConcurrently broadcastListenerTwo) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(this.rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setInstanceName("broadcast-consumer-two");
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-broadcast"));
        // 设置消费模式，默认是集群消费模式
        defaultMQPushConsumer.setMessageModel(MessageModel.BROADCASTING);
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-broadcast"), "*");
        defaultMQPushConsumer.setMessageListener(broadcastListenerTwo);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }
}
