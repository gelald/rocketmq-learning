package com.github.gelald.rocketmq.producer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import com.github.gelald.rocketmq.producer.client.property.RocketMQProducerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@Configuration
public class RocketMQProducerConfiguration implements DisposableBean {
    private static final List<MQProducer> mqProducers = new CopyOnWriteArrayList<>();
    private RocketMQProducerProperties rocketMQProducerProperties;

    @Bean
    @ConditionalOnProperty(prefix = "learning.rocketmq.producer", value = "type", havingValue = "DEFAULT", matchIfMissing = true)
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setNamesrvAddr(this.rocketMQProducerProperties.getNameServerAddr());
        defaultMQProducer.setProducerGroup((RocketMQConstant.PRODUCER_GROUP_PREFIX + this.rocketMQProducerProperties.getProducerGroupSuffix()));
        defaultMQProducer.start();
        mqProducers.add(defaultMQProducer);
        log.info("普通生产者创建成功");
        return defaultMQProducer;
    }

    @Bean
    @ConditionalOnProperty(prefix = "learning.rocketmq.producer", value = "type", havingValue = "TRANSACTION")
    public TransactionMQProducer transactionMQProducer() throws MQClientException {
        TransactionMQProducer transactionMQProducer = new TransactionMQProducer();
        transactionMQProducer.setNamesrvAddr(this.rocketMQProducerProperties.getNameServerAddr());
        transactionMQProducer.setProducerGroup((RocketMQConstant.PRODUCER_GROUP_PREFIX + this.rocketMQProducerProperties.getProducerGroupSuffix()));
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                System.out.println("接收到 MQ 的 half 消息响应，执行本地事务");
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                System.out.println("MQ 长时间无法收到消息的状态/本地执行事务状态为UNKNOW，执行补偿事务");
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        });
        transactionMQProducer.start();
        mqProducers.add(transactionMQProducer);
        log.info("事务生产者创建成功");
        return transactionMQProducer;
    }

    @Autowired
    public void setRocketMQProducerProperties(RocketMQProducerProperties rocketMQProducerProperties) {
        this.rocketMQProducerProperties = rocketMQProducerProperties;
    }

    @Override
    public void destroy() {
        if (!CollectionUtils.isEmpty(mqProducers)) {
            for (MQProducer mqProducer : mqProducers) {
                mqProducer.shutdown();
                log.info("RocketMQ生产者销毁成功");
            }
        }
    }
}
