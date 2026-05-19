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
 * @author WuYingBin
 * date: 2022/8/22
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "learning.rocketmq.consumer.consumer-switch", name = "transaction", havingValue = "true")
public class RocketMQTransactionConsumerConfiguration extends RocketMQBaseConsumerConfiguration {
    public RocketMQTransactionConsumerConfiguration(RocketMQConsumerProperties rocketMQConsumerProperties) {
        super(rocketMQConsumerProperties);
    }

    /**
     * 事务消息消费者
     */
    @Bean
    public PushConsumer transactionConsumer(MessageListener transactionListener) throws ClientException {
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(configuration)
                .setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-transaction"))
                .setSubscriptionExpressions(Collections.singletonMap((RocketMQConstant.TOPIC_PREFIX + "client-transaction"), FilterExpression.SUB_ALL))
                .setMessageListener(transactionListener)
                .build();
        mqConsumers.add(pushConsumer);
        return pushConsumer;
    }
}
