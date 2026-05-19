package com.github.gelald.rocketmq.consumer.client.configuration;

import com.github.gelald.rocketmq.consumer.client.property.RocketMQConsumerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author WuYingBin
 * date: 2022/8/19
 */
@Slf4j
@Configuration
public class RocketMQBaseConsumerConfiguration implements DisposableBean {

    protected static final List<Closeable> mqConsumers = new CopyOnWriteArrayList<>();
    protected static final ClientServiceProvider provider = ClientServiceProvider.loadService();
    protected static ClientConfiguration configuration;
    protected final RocketMQConsumerProperties rocketMQConsumerProperties;

    public RocketMQBaseConsumerConfiguration(RocketMQConsumerProperties rocketMQConsumerProperties) {
        this.rocketMQConsumerProperties = rocketMQConsumerProperties;
        configuration = ClientConfiguration.newBuilder()
                .setEndpoints(rocketMQConsumerProperties.getProxyAddr())
                .setRequestTimeout(Duration.ofSeconds(3)).build();
    }

    @Override
    public void destroy() throws IOException {
        if (!CollectionUtils.isEmpty(mqConsumers)) {
            for (Closeable mqConsumer : mqConsumers) {
                mqConsumer.close();
                log.info("RocketMQ消费者销毁成功");
            }
        }
    }
}
