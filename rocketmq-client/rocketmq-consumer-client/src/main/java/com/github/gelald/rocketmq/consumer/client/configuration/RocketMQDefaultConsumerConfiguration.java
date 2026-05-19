package com.github.gelald.rocketmq.consumer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import com.github.gelald.rocketmq.consumer.client.listener.DefaultListener;
import com.github.gelald.rocketmq.consumer.client.property.RocketMQConsumerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@Configuration
public class RocketMQDefaultConsumerConfiguration extends RocketMQBaseConsumerConfiguration {
    public RocketMQDefaultConsumerConfiguration(RocketMQConsumerProperties rocketMQConsumerProperties) {
        super(rocketMQConsumerProperties);
    }

    /**
     * 消费普通消息的消费者
     */
    @Bean
    public PushConsumer defaultMQPushConsumer(DefaultListener defaultListener) throws ClientException {
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(configuration)
                .setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client"))
                .setSubscriptionExpressions(Collections.singletonMap((RocketMQConstant.TOPIC_PREFIX + "client"), FilterExpression.SUB_ALL))
                .setMessageListener(defaultListener)
                .build();
        mqConsumers.add(pushConsumer);
        return pushConsumer;
    }
}
