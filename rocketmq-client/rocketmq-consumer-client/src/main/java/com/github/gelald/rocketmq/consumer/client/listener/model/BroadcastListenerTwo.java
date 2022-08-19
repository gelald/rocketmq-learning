package com.github.gelald.rocketmq.consumer.client.listener.model;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author WuYingBin
 * date: 2022/8/19
 */
@Slf4j
@Component
public class BroadcastListenerTwo implements MessageListenerConcurrently {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext context) {
        for (MessageExt messageExt : messageExtList) {
            String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            log.info("BroadcastListenerTwo接收到消息, 消息内容: {}", body);
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
