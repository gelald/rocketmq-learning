package com.github.gelald.rocketmq.consumer.client.listener.order;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author WuYingBin
 * date: 2022/8/19
 */
@Slf4j
@Component
public class GlobalOrderListener implements MessageListenerOrderly {
    private final Lock lock = new ReentrantLock();
    // 随机消费失败3次演示顺序消息遇到消费不到的消息的处理方式
    private int times = 0;
    // 记录上一次消费失败消息的number属性值，下一次消费时不再失败
    private int lastNumber = -1;

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeOrderlyContext context) {
        // 能保证每次只有一条消息，可以把return语句放到for循环内
        for (MessageExt messageExt : messageExtList) {
            String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            if (times < 3) {
                int number = Integer.parseInt(messageExt.getProperty("number"));
                if (lastNumber != number && number % 3 == 0) {
                    log.info("GlobalOrderListener消费消息失败，稍后再消费");
                    try {
                        lock.lock();
                        times++;
                        lastNumber = number;
                    } finally {
                        lock.unlock();
                    }
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                } else {
                    log.info("GlobalOrderListener成功消费消息: {}", body);
                    return ConsumeOrderlyStatus.SUCCESS;
                }
            } else {
                log.info("GlobalOrderListener成功消费消息: {}", body);
                return ConsumeOrderlyStatus.SUCCESS;
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }

}
