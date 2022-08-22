package com.github.gelald.rocketmq.consumer.client.listener.filter;

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
 * date: 2022/8/22
 */
@Slf4j
@Component
public class TagListenerOne implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext context) {
        if (CollectionUtils.isEmpty(messageExtList)) {
            log.info("本次消息为空");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        for (MessageExt messageExt : messageExtList) {
            String topic = messageExt.getTopic();
            String tags = messageExt.getTags();
            String price = messageExt.getProperty("price");
            String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            log.info("消息监听器: client-tag-tag-filter 接收到消息topic: {}, tags: {}, price: {}, 消息内容: {}", topic, tags, price, body);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                log.info("消费者业务逻辑发生异常", e);
                log.info("本次消息将放入重试队列");
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
