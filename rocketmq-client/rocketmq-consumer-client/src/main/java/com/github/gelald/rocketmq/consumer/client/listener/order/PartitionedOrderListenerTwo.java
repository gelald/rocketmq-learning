package com.github.gelald.rocketmq.consumer.client.listener.order;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
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
public class PartitionedOrderListenerTwo implements MessageListenerOrderly {
    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeOrderlyContext context) {
        for (MessageExt messageExt : messageExtList) {
            String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            log.info("PartitionedOrderListenerTwo成功消费消息: {}", body);
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
