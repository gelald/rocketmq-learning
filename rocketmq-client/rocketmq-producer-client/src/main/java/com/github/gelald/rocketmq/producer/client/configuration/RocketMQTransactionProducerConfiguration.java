package com.github.gelald.rocketmq.producer.client.configuration;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author WuYingBin
 * date: 2022/8/20
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "learning.rocketmq.producer.producer-switch", name = "transaction", havingValue = "true")
public class RocketMQTransactionProducerConfiguration extends RocketMQBaseProducerConfiguration {

    @Bean
    public TransactionMQProducer transactionMQProducer(TransactionListener bizTransactionListener) throws MQClientException {
        // 定义事务型生产者
        TransactionMQProducer transactionMQProducer = new TransactionMQProducer();
        transactionMQProducer.setNamesrvAddr(rocketMQProducerProperties.getNameServerAddr());
        transactionMQProducer.setProducerGroup((RocketMQConstant.PRODUCER_GROUP_PREFIX + "client-transactional"));
        // 定义事务监听器
        transactionMQProducer.setTransactionListener(bizTransactionListener);
        transactionMQProducer.start();
        mqProducers.add(transactionMQProducer);
        return transactionMQProducer;
    }

    @Bean
    public TransactionListener bizTransactionListener() {
        // 实现事务监听器两个接口
        return new TransactionListener() {

            // 执行生产者方本地事务
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                log.info("接收到RocketMQ的Half消息的响应，现在执行本地事务...");
                int number = (Integer) arg;
                try {
                    Integer result = 100 / number;
                    log.info("事务执行结果: {}", result);
                    // 线程睡眠500毫秒模拟本地事务执行
                    TimeUnit.MILLISECONDS.sleep(500);
                    log.info("本地事务执行成功，给RocketMQ发送ACK响应");
                    return LocalTransactionState.COMMIT_MESSAGE;
                } catch (Exception e) {
                    log.info("本地事务执行发生异常，需要回滚事务");
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }

            // 回查本地事务执行情况
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                log.info("由于RocketMQ长时间无法收到消息的状态或本地执行事务状态为UNKNOW，现在执行补偿事务/回查本地事务...");
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        };
    }

}
