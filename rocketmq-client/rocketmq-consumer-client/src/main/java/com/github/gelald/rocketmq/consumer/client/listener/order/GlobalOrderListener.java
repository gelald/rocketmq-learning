package com.github.gelald.rocketmq.consumer.client.listener.order;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author WuYingBin
 * date: 2022/8/19
 */
@Slf4j
@Component
public class GlobalOrderListener implements MessageListener {
    private final SecureRandom secureRandom = new SecureRandom();
    private final Lock lock = new ReentrantLock();
    // 随机消费失败3次演示顺序消息遇到消费不到的消息的处理方式
    private int times = 0;
    // 记录上一次消费失败消息的number属性值，下一次消费时不再失败
    private int lastNumber = -1;

    @Override
    public ConsumeResult consume(MessageView messageView) {
        if (times < 3) {
            int number = secureRandom.nextInt(1, 10);
            // 如果是3的倍数且失败次数还没达到，那么手动让本次消息消费失败
            if (lastNumber != number && number % 3 == 0) {
                log.info("GlobalOrderListener消费消息失败, 稍后再消费");
                try {
                    lock.lock();
                    times++;
                    lastNumber = number;
                } finally {
                    lock.unlock();
                }
                return ConsumeResult.FAILURE;
            } else {
                log.info("GlobalOrderListener成功消费消息-inner: {}", messageView);
                return ConsumeResult.SUCCESS;
            }
        } else {
            log.info("GlobalOrderListener成功消费消息-outer: {}", messageView);
            return ConsumeResult.SUCCESS;
        }
    }
}
