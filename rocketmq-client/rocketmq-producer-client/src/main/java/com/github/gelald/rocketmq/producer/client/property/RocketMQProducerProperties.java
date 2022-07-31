package com.github.gelald.rocketmq.producer.client.property;

import com.github.gelald.rocketmq.common.enums.RocketMQProducerEnum;
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
    private RocketMQProducerEnum producerType;
    /**
     * RocketMQ中NameServer地址
     */
    private String nameServerAddr;
    /**
     * 生产者组名称后缀
     * 前缀见 {@link com.github.gelald.rocketmq.common.constant.RocketMQConstant#PRODUCER_GROUP_PREFIX}
     */
    private String producerGroupSuffix;


    public void setProducerType(RocketMQProducerEnum producerType) {
        this.producerType = producerType;
    }

    public void setNameServerAddr(String nameServerAddr) {
        this.nameServerAddr = nameServerAddr;
    }

    public void setProducerGroupSuffix(String producerGroupSuffix) {
        this.producerGroupSuffix = producerGroupSuffix;
    }
}
