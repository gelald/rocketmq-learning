package com.github.gelald.rocketmq.consumer.client.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@Component
public class DefaultListener implements MessageListenerConcurrently {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext context) {
        if (CollectionUtils.isEmpty(messageExtList)) {
            log.info("本次消息为空");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        for (MessageExt messageExt : messageExtList) {
            String topic = messageExt.getTopic();
            String tags = messageExt.getTags();
            String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            log.info("消息topic: {}, tags: {}, 消息内容: {}", topic, tags, body);
            if (messageExt.getDelayTimeLevel() != 0) {
                log.info("本次消息延时等级: {}, 延时时长为: {}", messageExt.getDelayTimeLevel(), messageExt.getProperty("delayTime"));
            }
            try {
                // 线程休眠模拟消费者业务执行
                TimeUnit.MILLISECONDS.sleep(1500);
            } catch (InterruptedException e) {
                log.info("消费者业务逻辑发生异常", e);
                log.info("本次消息将放入重试队列");
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
