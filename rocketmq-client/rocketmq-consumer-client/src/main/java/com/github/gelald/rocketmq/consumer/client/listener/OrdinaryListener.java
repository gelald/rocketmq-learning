package com.github.gelald.rocketmq.consumer.client.listener;

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
@Component
public class OrdinaryListener implements MessageListenerConcurrently {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext context) {
        if (CollectionUtils.isEmpty(messageExtList)) {
            System.out.println("MQ 接收的消息为空");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        for (MessageExt messageExt : messageExtList) {
            String topic = messageExt.getTopic();
            String tags = messageExt.getTags();
            String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            System.out.println("MQ消息topic=" + topic + ", tags=" + tags + ", 消息内容=" + body);
            try {
                TimeUnit.MILLISECONDS.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
