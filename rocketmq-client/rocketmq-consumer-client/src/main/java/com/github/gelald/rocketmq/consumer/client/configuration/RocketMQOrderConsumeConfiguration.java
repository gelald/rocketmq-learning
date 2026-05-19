package com.github.gelald.rocketmq.consumer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import com.github.gelald.rocketmq.consumer.client.property.RocketMQConsumerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

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
    public RocketMQOrderConsumeConfiguration(RocketMQConsumerProperties rocketMQConsumerProperties) {
        super(rocketMQConsumerProperties);
    }

    /**
     * 全局有序的消费者
     */
    @Bean
    public PushConsumer globalOrderConsumer(MessageListener globalOrderListener) throws ClientException {
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(configuration)
                .setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-global-order"))
                .setSubscriptionExpressions(Collections.singletonMap((RocketMQConstant.TOPIC_PREFIX + "client-global-order"), FilterExpression.SUB_ALL))
                .setMessageListener(globalOrderListener)
                .build();
        mqConsumers.add(pushConsumer);
        return pushConsumer;
    }

    /**
     * 分区有序的消费者1
     */
    @Bean
    public PushConsumer partitionedOrderConsumerOne(MessageListener partitionedOrderListenerOne) throws ClientException {
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(configuration)
                .setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-partitioned-order"))
                .setSubscriptionExpressions(Collections.singletonMap((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"), FilterExpression.SUB_ALL))
                .setMessageListener(partitionedOrderListenerOne)
                .build();
        mqConsumers.add(pushConsumer);
        return pushConsumer;
    }

    /**
     * 分区有序的消费者2
     */
    @Bean
    public PushConsumer partitionedOrderConsumerTwo(MessageListener partitionedOrderListenerTwo) throws ClientException {
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(configuration)
                .setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-partitioned-order"))
                .setSubscriptionExpressions(Collections.singletonMap((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"), FilterExpression.SUB_ALL))
                .setMessageListener(partitionedOrderListenerTwo)
                .build();
        mqConsumers.add(pushConsumer);
        return pushConsumer;
    }
}
