package com.github.gelald.rocketmq.producer.client.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Data
@Component
@ConfigurationProperties(prefix = "learning.rocketmq.producer")
public class RocketMQProducerProperties {
    /**
     * 创建哪一种类型的生产者
     */
    private MQProducerEnum producerType;
    /**
     * RocketMQ中NameServer地址
     */
    private String nameServerAddr;
    /**
     * 生产者组名称
     */
    private String producerGroup;

    public enum MQProducerEnum {
        DEFAULT,
        TRANSACTION
    }
}
