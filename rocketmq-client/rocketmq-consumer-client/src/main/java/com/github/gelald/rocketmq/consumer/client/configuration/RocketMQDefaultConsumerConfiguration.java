package com.github.gelald.rocketmq.consumer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@Configuration
public class RocketMQDefaultConsumerConfiguration extends RocketMQConsumerBaseConfiguration {

    /**
     * 消费普通消息的消费者
     */
    @Bean
    public DefaultMQPushConsumer ordinaryConsumer(MessageListenerConcurrently ordinaryListener) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client"));
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-ordinary"), "*");
        defaultMQPushConsumer.setMessageListener(ordinaryListener);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }

}
