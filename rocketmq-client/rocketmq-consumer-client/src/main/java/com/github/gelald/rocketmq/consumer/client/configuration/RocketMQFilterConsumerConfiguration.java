package com.github.gelald.rocketmq.consumer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import com.github.gelald.rocketmq.consumer.client.property.RocketMQConsumerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * @author WuYingBin
 * date: 2022/8/21
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "learning.rocketmq.consumer.consumer-switch", name = "filter", havingValue = "true")
public class RocketMQFilterConsumerConfiguration extends RocketMQBaseConsumerConfiguration {
    public RocketMQFilterConsumerConfiguration(RocketMQConsumerProperties rocketMQConsumerProperties) {
        super(rocketMQConsumerProperties);
    }

    /**
     * 使用Tag过滤的消费者
     */
    @Bean
    public PushConsumer tagFilterConsumer(MessageListener defaultListener) throws ClientException {
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(configuration)
                .setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-tag-filter"))
                .setSubscriptionExpressions(Collections.singletonMap((RocketMQConstant.TOPIC_PREFIX + "client-tag-filter"),
                        new FilterExpression("phone || shoes")))
                .setMessageListener(defaultListener)
                .build();
        mqConsumers.add(pushConsumer);
        return pushConsumer;
    }

    /**
     * 使用SQL过滤的消费者
     */
    @Bean
    public PushConsumer sqlFilterConsumer(MessageListener defaultListener) throws ClientException {
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(configuration)
                .setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-tag-filter"))
                .setSubscriptionExpressions(Collections.singletonMap((RocketMQConstant.TOPIC_PREFIX + "client-sql-filter"),
                        new FilterExpression("price is not null and price between 10 and 30", FilterExpressionType.SQL92)))
                .setMessageListener(defaultListener)
                .build();
        mqConsumers.add(pushConsumer);
        return pushConsumer;
    }
}
