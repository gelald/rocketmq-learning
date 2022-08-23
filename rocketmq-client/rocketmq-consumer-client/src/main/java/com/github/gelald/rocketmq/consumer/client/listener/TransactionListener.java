package com.github.gelald.rocketmq.consumer.client.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author WuYingBin
 * date: 2022/8/22
 */
@Slf4j
@Component
public class TransactionListener implements MessageListenerConcurrently {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext context) {
        for (MessageExt messageExt : messageExtList) {
            log.info("接收到事务消息，执行消费者本地事务");
            log.info("消息内容: {}", messageExt);
            try {
                log.info("本地事务执行中...");
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                log.info("本地事务执行失败，重试消息或人工干预");
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            log.info("本地事务执行成功");
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
