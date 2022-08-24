package com.github.gelald.rocketmq.producer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定义测试发送顺序消息的生产者
 *
 * @author WuYingBin
 * date: 2022/8/20
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "learning.rocketmq.producer.producer-switch", name = "order", havingValue = "true")
public class RocketMQOrderProducerConfiguration extends RocketMQBaseProducerConfiguration {

    @Bean
    public DefaultMQProducer globalMQProducer() throws MQClientException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setNamesrvAddr(rocketMQProducerProperties.getNameServerAddr());
        defaultMQProducer.setProducerGroup((RocketMQConstant.PRODUCER_GROUP_PREFIX + "client-global-order"));
        // 全局有序消息，生产者只定义一个队列
        defaultMQProducer.setDefaultTopicQueueNums(1);
        defaultMQProducer.start();
        mqProducers.add(defaultMQProducer);
        return defaultMQProducer;
    }

    @Bean
    public DefaultMQProducer partitionedMQProducer() throws MQClientException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setNamesrvAddr(rocketMQProducerProperties.getNameServerAddr());
        defaultMQProducer.setProducerGroup((RocketMQConstant.PRODUCER_GROUP_PREFIX + "client-partitioned-order"));
        // 由于消费者方定义了两个消费者来演示此功能，因此定义两个队列来对应两个消费者
        defaultMQProducer.setDefaultTopicQueueNums(2);
        defaultMQProducer.start();
        mqProducers.add(defaultMQProducer);
        return defaultMQProducer;
    }

}
