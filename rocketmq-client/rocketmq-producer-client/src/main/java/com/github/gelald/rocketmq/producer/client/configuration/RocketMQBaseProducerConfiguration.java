package com.github.gelald.rocketmq.producer.client.configuration;

import com.github.gelald.rocketmq.producer.client.property.RocketMQProducerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author WuYingBin
 * date: 2022/8/20
 */
@Slf4j
@Configuration
public class RocketMQBaseProducerConfiguration implements DisposableBean {

    public static final List<MQProducer> mqProducers = new CopyOnWriteArrayList<>();
    protected RocketMQProducerProperties rocketMQProducerProperties;


    @Override
    public void destroy() {
        if (!CollectionUtils.isEmpty(mqProducers)) {
            for (MQProducer mqProducer : mqProducers) {
                mqProducer.shutdown();
                log.info("RocketMQ生产者销毁成功");
            }
        }
    }

    @Autowired
    public void setRocketMQProducerProperties(RocketMQProducerProperties rocketMQProducerProperties) {
        this.rocketMQProducerProperties = rocketMQProducerProperties;
    }
}
