package com.github.gelald.rocketmq.consumer.client.listener.order;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.springframework.stereotype.Component;

/**
 * @author WuYingBin
 * date: 2022/8/19
 */
@Slf4j
@Component
public class PartitionedOrderListenerTwo implements MessageListener {
    @Override
    public ConsumeResult consume(MessageView messageView) {
        log.info("PartitionedOrderListenerTwo成功消费消息: {}", messageView);
        return ConsumeResult.SUCCESS;
    }
}
