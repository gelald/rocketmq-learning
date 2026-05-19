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
     * Proxy组件地址
     */
    private String proxyAddr;
    /**
     * 控制其他类型消费者是否创建
     * 默认的消费者不受控制
     */
    private ConsumerSwitch consumerSwitch;

    @Data
    public static class ConsumerSwitch {
        /**
         * 是否创建发送顺序消息的消费者
         */
        private Boolean order = false;
        /**
         * 是否创建测试过滤消息的消费者
         */
        private Boolean filter = false;
        /**
         * 是否创建发送事务消息的消费者
         */
        private Boolean transaction = false;
    }
}
