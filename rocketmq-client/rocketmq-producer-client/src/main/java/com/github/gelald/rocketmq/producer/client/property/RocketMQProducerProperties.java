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
     * Proxy组件地址
     */
    private String proxyAddr;
}
