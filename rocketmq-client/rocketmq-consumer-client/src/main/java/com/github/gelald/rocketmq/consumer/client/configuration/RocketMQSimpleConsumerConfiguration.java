package com.github.gelald.rocketmq.consumer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import com.github.gelald.rocketmq.consumer.client.property.RocketMQConsumerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.SimpleConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collections;

@Slf4j
@Configuration
public class RocketMQSimpleConsumerConfiguration extends RocketMQBaseConsumerConfiguration {
    public RocketMQSimpleConsumerConfiguration(RocketMQConsumerProperties rocketMQConsumerProperties) {
        super(rocketMQConsumerProperties);
    }

    @Bean
    public SimpleConsumer defaultSimpleConsumer() throws ClientException {
        SimpleConsumer simpleConsumer = provider.newSimpleConsumerBuilder()
                .setClientConfiguration(configuration)
                // Set the consumer group name.
                .setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "simple-client"))
                // set await duration for long-polling.
                // "Broker 没消息时，Client 最多等待多长时间才返回空"
                .setAwaitDuration(Duration.ofSeconds(30))
                // Set the subscription for the consumer.
                .setSubscriptionExpressions(Collections.singletonMap((RocketMQConstant.TOPIC_PREFIX + "simple-client"), FilterExpression.SUB_ALL))
                .build();
        mqConsumers.add(simpleConsumer);
        return simpleConsumer;
    }
}
