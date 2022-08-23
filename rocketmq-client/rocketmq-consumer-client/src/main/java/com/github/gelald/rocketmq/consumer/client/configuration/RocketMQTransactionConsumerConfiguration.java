package com.github.gelald.rocketmq.consumer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WuYingBin
 * date: 2022/8/22
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "learning.rocketmq.consumer.consumer-switch", name = "transaction", havingValue = "true")
public class RocketMQTransactionConsumerConfiguration extends RocketMQBaseConsumerConfiguration {
    /**
     * 事务消息消费者
     */
    @Bean
    public DefaultMQPushConsumer transactionConsumer(MessageListenerConcurrently transactionListener) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(this.rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-transaction"));
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-transaction"), "*");
        defaultMQPushConsumer.setMessageListener(transactionListener);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }
}
