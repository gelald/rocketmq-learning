package com.github.gelald.rocketmq.consumer.client.configuration;

import com.github.gelald.rocketmq.consumer.client.property.RocketMQConsumerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author WuYingBin
 * date: 2022/8/19
 */
@Slf4j
@Configuration
public class RocketMQConsumerBaseConfiguration implements DisposableBean {

    protected final List<MQPushConsumer> mqConsumers = new CopyOnWriteArrayList<>();

    protected RocketMQConsumerProperties rocketMQConsumerProperties;

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
