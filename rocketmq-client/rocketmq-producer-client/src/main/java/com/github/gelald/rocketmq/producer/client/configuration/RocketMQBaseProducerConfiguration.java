package com.github.gelald.rocketmq.producer.client.configuration;

import com.github.gelald.rocketmq.producer.client.property.RocketMQProducerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author WuYingBin
 * date: 2022/8/20
 */
@Slf4j
@Configuration
public class RocketMQBaseProducerConfiguration implements DisposableBean {

    protected static final List<Producer> mqProducers = new CopyOnWriteArrayList<>();
    protected static final ClientServiceProvider provider = ClientServiceProvider.loadService();
    protected static ClientConfiguration configuration;
    protected final RocketMQProducerProperties rocketMQProducerProperties;

    public RocketMQBaseProducerConfiguration(RocketMQProducerProperties rocketMQProducerProperties) {
        this.rocketMQProducerProperties = rocketMQProducerProperties;
        configuration = ClientConfiguration.newBuilder()
                .setEndpoints(rocketMQProducerProperties.getProxyAddr())
                .setRequestTimeout(Duration.ofSeconds(3)).build();
    }

    @Override
    public void destroy() throws IOException {
        if (!CollectionUtils.isEmpty(mqProducers)) {
            for (Producer mqProducer : mqProducers) {
                mqProducer.close();
                log.info("RocketMQ生产者销毁成功");
            }
        }
    }
}
