package com.github.gelald.rocketmq.consumer.client.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author WuYingBin
 * date: 2022/8/22
 */
@Slf4j
@Component
public class TransactionListener implements MessageListener {
    @Override
    public ConsumeResult consume(MessageView messageView) {
        log.info("接收到事务消息，执行消费者本地事务");
        log.info("消息内容: {}", messageView);
        try {
            log.info("本地事务执行中...");
            TimeUnit.MILLISECONDS.sleep(2000);
            log.info("本地事务执行成功");
            return ConsumeResult.SUCCESS;
        } catch (InterruptedException e) {
            log.info("本地事务执行失败，重试消息或人工干预");
            return ConsumeResult.FAILURE;
        }
    }
}
