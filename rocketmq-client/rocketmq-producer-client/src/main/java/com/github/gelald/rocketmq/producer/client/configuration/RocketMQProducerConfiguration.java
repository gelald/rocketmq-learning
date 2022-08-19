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
import java.util.concurrent.TimeUnit;

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
        defaultMQProducer.setProducerGroup((RocketMQConstant.PRODUCER_GROUP_PREFIX + "client"));
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
        transactionMQProducer.setProducerGroup((RocketMQConstant.PRODUCER_GROUP_PREFIX + "client"));
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                log.info("接收到RocketMQ的Half消息的响应，现在执行本地事务...");
                try {
                    // 线程睡眠500毫秒模拟本地事务执行
                    TimeUnit.MILLISECONDS.sleep(500);
                    log.info("本地事务执行成功，给RocketMQ发送ACK响应");
                    return LocalTransactionState.COMMIT_MESSAGE;
                } catch (InterruptedException e) {
                    log.info("本地事务执行发生异常，需要回滚事务");
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                log.info("由于RocketMQ长时间无法收到消息的状态或本地执行事务状态为UNKNOW，现在执行补偿事务/回查本地事务...");
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
