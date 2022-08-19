package com.github.gelald.rocketmq.consumer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import com.github.gelald.rocketmq.consumer.client.property.RocketMQConsumerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@Configuration
public class RocketMQConsumerConfiguration implements DisposableBean {

    private static final List<MQPushConsumer> mqConsumers = new CopyOnWriteArrayList<>();

    private RocketMQConsumerProperties rocketMQConsumerProperties;

    /**
     * 消费普通消息的消费者
     */
    @Bean
    public DefaultMQPushConsumer ordinaryConsumer(MessageListenerConcurrently ordinaryListener) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(this.rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client"));
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-ordinary"), "*");
        defaultMQPushConsumer.setMessageListener(ordinaryListener);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        log.info("普通消息消费者创建成功");
        return defaultMQPushConsumer;
    }

    /**
     * 集群消费的消费者1
     */
    @Bean
    public DefaultMQPushConsumer clusteringMQPushConsumerOne(MessageListenerConcurrently clusteringListenerOne) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(this.rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setInstanceName("clustering-consumer-one");
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-clustering"));
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-clustering"), "*");
        defaultMQPushConsumer.setMessageListener(clusteringListenerOne);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }

    /**
     * 集群消费的消费者2
     */
    @Bean
    public DefaultMQPushConsumer clusteringMQPushConsumerTwo(MessageListenerConcurrently clusteringListenerTwo) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(this.rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setInstanceName("clustering-consumer-two");
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-clustering"));
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-clustering"), "*");
        defaultMQPushConsumer.setMessageListener(clusteringListenerTwo);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }

    /**
     * 广播消费的消费者1
     */
    @Bean
    public DefaultMQPushConsumer broadcastMQPushConsumerOne(MessageListenerConcurrently broadcastListenerOne) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(this.rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setInstanceName("broadcast-consumer-one");
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-broadcast"));
        defaultMQPushConsumer.setMessageModel(MessageModel.BROADCASTING);
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-broadcast"), "*");
        defaultMQPushConsumer.setMessageListener(broadcastListenerOne);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }

    /**
     * 广播消费的消费者2
     */
    @Bean
    public DefaultMQPushConsumer broadcastMQPushConsumerTwo(MessageListenerConcurrently broadcastListenerTwo) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(this.rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setInstanceName("broadcast-consumer-two");
        defaultMQPushConsumer.setConsumerGroup((RocketMQConstant.CONSUMER_GROUP_PREFIX + "client-broadcast"));
        defaultMQPushConsumer.setMessageModel(MessageModel.BROADCASTING);
        defaultMQPushConsumer.subscribe((RocketMQConstant.TOPIC_PREFIX + "client-broadcast"), "*");
        defaultMQPushConsumer.setMessageListener(broadcastListenerTwo);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        return defaultMQPushConsumer;
    }

    @Override
    public void destroy() {
        if (!CollectionUtils.isEmpty(mqConsumers)) {
            for (MQPushConsumer mqConsumer : mqConsumers) {
                mqConsumer.shutdown();
                log.info("RocketMQ消费者销毁成功");
            }
        }
    }

    @Autowired
    public void setRocketMQConsumerProperties(RocketMQConsumerProperties rocketMQConsumerProperties) {
        this.rocketMQConsumerProperties = rocketMQConsumerProperties;
    }

}
