package com.github.gelald.rocketmq.producer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@Configuration
public class RocketMQDefaultProducerConfiguration extends RocketMQBaseProducerConfiguration {
    @Bean
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        // 创建消息生产者
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        // 设置生产者NameServer地址，用于寻找Broker
        defaultMQProducer.setNamesrvAddr(rocketMQProducerProperties.getNameServerAddr());
        // 设置生产者组
        defaultMQProducer.setProducerGroup((RocketMQConstant.PRODUCER_GROUP_PREFIX + "client"));
        // 启动生产者组
        defaultMQProducer.start();
        // 把创建的生产者放到一个集合，当程序结束时统一销毁
        mqProducers.add(defaultMQProducer);
        return defaultMQProducer;
    }
}
