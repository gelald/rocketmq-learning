package com.github.gelald.rocketmq.consumer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WuYingBin
 * date: 2022/8/21
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "learning.rocketmq.consumer.consumer-switch", name = "filter", havingValue = "true")
public class RocketMQFilterConsumerConfiguration extends RocketMQBaseConsumerConfiguration {
    /**
     * 使用Tag过滤的消费者
     */
    @Bean
    public DefaultMQPushConsumer tagFilterConsumer(MessageListenerConcurrently defaultListener) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-tag-filter"));
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-tag-filter"),
                MessageSelector.byTag("phone || shoes"));
        defaultMQPushConsumer.setMessageListener(defaultListener);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }

    /**
     * 使用SQL过滤的消费者
     */
    @Bean
    public DefaultMQPushConsumer sqlFilterConsumer(MessageListenerConcurrently defaultListener) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-sql-filter"));
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-sql-filter"),
                MessageSelector.bySql("price is not null and price between 10 and 30"));
        defaultMQPushConsumer.setMessageListener(defaultListener);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }
}
