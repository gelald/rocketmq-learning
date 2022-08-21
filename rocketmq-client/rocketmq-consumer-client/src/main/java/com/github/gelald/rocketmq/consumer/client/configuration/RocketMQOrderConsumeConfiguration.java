package com.github.gelald.rocketmq.consumer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定义测试顺序消费的消费者
 *
 * @author WuYingBin
 * date: 2022/8/19
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "learning.rocketmq.consumer.consumer-switch", name = "order", havingValue = "true")
public class RocketMQOrderConsumeConfiguration extends RocketMQBaseConsumerConfiguration {
    /**
     * 全局有序的消费者
     */
    @Bean
    public DefaultMQPushConsumer globalOrderConsumer(MessageListenerOrderly globalOrderListener) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setInstanceName("global-order-consumer");
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-global-order"));
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-global-order"), "*");
        defaultMQPushConsumer.setMessageListener(globalOrderListener);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }

    /**
     * 分区有序的消费者1
     */
    @Bean
    public DefaultMQPushConsumer partitionedOrderConsumerOne(MessageListenerOrderly partitionedOrderListenerOne) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(this.rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setInstanceName("partitioned-order-consumer-one");
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-partitioned-order"));
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"), "*");
        defaultMQPushConsumer.setMessageListener(partitionedOrderListenerOne);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }

    /**
     * 分区有序的消费者2
     */
    @Bean
    public DefaultMQPushConsumer partitionedOrderConsumerTwo(MessageListenerOrderly partitionedOrderListenerTwo) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(this.rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setInstanceName("partitioned-order-consumer-two");
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-partitioned-order"));
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"), "*");
        defaultMQPushConsumer.setMessageListener(partitionedOrderListenerTwo);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }
}
