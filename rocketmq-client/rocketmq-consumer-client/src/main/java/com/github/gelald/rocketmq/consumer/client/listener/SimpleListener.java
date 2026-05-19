package com.github.gelald.rocketmq.consumer.client.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.consumer.SimpleConsumer;
import org.apache.rocketmq.client.apis.message.MessageId;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class SimpleListener implements InitializingBean {
    private final SimpleConsumer defaultSimpleConsumer;

    @Override
    public void afterPropertiesSet() throws Exception {
        // You can calculate the number of messages that need to be received each time
        // and the invisible time based on the estimated processing time of each message.
        // Max message num for each long polling.
        // 单次拉取消息的总数
        int maxMessageNum = 16;
        // Set message invisible duration after it is received.
        // "这条消息被我拉走后，对其他Client不可见的时间"
        Duration invisibleDuration = Duration.ofSeconds(15);
        // Receive message, multi-threading is more recommended.
        do {
            final List<MessageView> messages = defaultSimpleConsumer.receive(maxMessageNum, invisibleDuration);
            log.info("Received {} message(s)", messages.size());
            for (MessageView message : messages) {
                final MessageId messageId = message.getMessageId();
                try {
                    defaultSimpleConsumer.ack(message);
                    log.info("Message is acknowledged successfully, messageId={}", messageId);
                } catch (Throwable t) {
                    log.error("Message is failed to be acknowledged, messageId={}", messageId, t);
                }
            }
        } while (true);
    }
}
