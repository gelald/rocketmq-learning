package com.github.gelald.rocketmq.producer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@Configuration
public class RocketMQDefaultProducerConfiguration extends RocketMQProducerBaseConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "learning.rocketmq.producer", value = "type", havingValue = "DEFAULT", matchIfMissing = true)
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setNamesrvAddr(rocketMQProducerProperties.getNameServerAddr());
        defaultMQProducer.setProducerGroup((RocketMQConstant.PRODUCER_GROUP_PREFIX + "client"));
        defaultMQProducer.start();
        mqProducers.add(defaultMQProducer);
        return defaultMQProducer;
    }

}
