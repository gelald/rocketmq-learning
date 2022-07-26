package com.github.gelald.rocketmq.consumer.client.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Data
@Component
@ConfigurationProperties(prefix = "learning.rocketmq.consumer")
public class RocketMQConsumerProperties {
    /**
     * RocketMQ中NameServer地址
     */
    private String nameServerAddr;
}
