package com.github.gelald.rocketmq.consumer.client.configuration;

import com.github.gelald.rocketmq.consumer.client.listener.OrdinaryListener;
import com.github.gelald.rocketmq.consumer.client.property.RocketMQConsumerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
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
    private OrdinaryListener ordinaryListener;

    @Bean
    public DefaultMQPushConsumer ordinaryConsumer() throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(this.rocketMQConsumerProperties.getNameServerAddr());
        defaultMQPushConsumer.setConsumerGroup("ROCKETMQ_CLIENT_ORDINARY");
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        defaultMQPushConsumer.subscribe("RocketMQClientOrdinaryTopic", "*");
        defaultMQPushConsumer.setMessageListener(this.ordinaryListener);
        defaultMQPushConsumer.start();
        mqConsumers.add(defaultMQPushConsumer);
        log.info("普通消息消费者创建成功");
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

    @Autowired
    public void setCommonListener(OrdinaryListener ordinaryListener) {
        this.ordinaryListener = ordinaryListener;
    }
}
