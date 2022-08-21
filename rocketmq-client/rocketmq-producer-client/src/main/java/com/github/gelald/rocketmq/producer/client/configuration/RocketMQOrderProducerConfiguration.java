package com.github.gelald.rocketmq.producer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
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
public class RocketMQOrderProducerConfiguration extends RocketMQProducerBaseConfiguration {

    @Bean
    public DefaultMQProducer globalMQProducer() throws MQClientException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setNamesrvAddr(rocketMQProducerProperties.getNameServerAddr());
        defaultMQProducer.setProducerGroup((RocketMQConstant.PRODUCER_GROUP_PREFIX + "client-global-order"));
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
        defaultMQProducer.setDefaultTopicQueueNums(2);
        defaultMQProducer.start();
        mqProducers.add(defaultMQProducer);
        return defaultMQProducer;
    }

}
