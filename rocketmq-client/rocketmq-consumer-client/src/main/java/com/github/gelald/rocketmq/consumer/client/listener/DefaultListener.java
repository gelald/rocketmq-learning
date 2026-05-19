package com.github.gelald.rocketmq.consumer.client.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.springframework.stereotype.Component;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@Component
public class DefaultListener implements MessageListener {
    @Override
    public ConsumeResult consume(MessageView messageView) {
        log.info("消费消息: {}", messageView);
        return ConsumeResult.SUCCESS;
    }
}
