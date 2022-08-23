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
     * RocketMQ中NameServer地址
     */
    private String nameServerAddr;
    /**
     * 控制其他类型生产者是否创建
     * 默认的生产者不受控制
     */
    private ProducerSwitch producerSwitch;

    @Data
    private static class ProducerSwitch {
        /**
         * 是否创建发送顺序消息的生产者
         */
        private Boolean order = false;
        /**
         * 是否创建发送事务消息的生产者
         */
        private Boolean transaction = false;
    }

    public void setNameServerAddr(String nameServerAddr) {
        this.nameServerAddr = nameServerAddr;
    }
}
