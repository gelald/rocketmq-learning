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
public class RocketMQDefaultConsumerConfiguration extends RocketMQBaseConsumerConfiguration {
    /**
     * 消费普通消息的消费者
     */
    @Bean
    public DefaultMQPushConsumer defaultMQPushConsumer(MessageListenerConcurrently defaultListener) throws MQClientException {
        // 创建消息消费者
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        // 设置消费者NameServer地址，用于寻找Broker
        defaultMQPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameServerAddr());
        // 设置消费者组
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client"));
        // 设置消费者组订阅的Topic等信息
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client"), "*");
        // 设置消费者消息监听器
        defaultMQPushConsumer.setMessageListener(defaultListener);
        // 启动消费者
        defaultMQPushConsumer.start();
        // 把创建的消费者放到一个集合中，当程序结束时统一销毁
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }
}
