package com.github.gelald.rocketmq.consumer.starter.consumer;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author WuYingBin
 * Date 2022/8/24
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = (RocketMQConstant.CONSUMER_GROUP_PREFIX + "starter-transaction"),
        topic = (RocketMQConstant.TOPIC_PREFIX + "starter-transaction")
)
public class TransactionConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("接收到事务消息，准备执行消费者本地事务");
        log.info("事务消息内容: {}", message);
        try {
            log.info("本地事务执行中...");
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            log.info("本地事务执行失败，稍后重新消费消息或人工干预");
            return;
        }
        log.info("本地事务执行成功");
    }
}
