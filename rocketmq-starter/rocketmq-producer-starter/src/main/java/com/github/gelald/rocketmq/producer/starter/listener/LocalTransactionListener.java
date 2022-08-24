package com.github.gelald.rocketmq.producer.starter.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

import java.util.concurrent.TimeUnit;

/**
 * @author WuYingBin
 * Date 2022/8/24
 */
@Slf4j
@RocketMQTransactionListener
public class LocalTransactionListener implements RocketMQLocalTransactionListener {
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("接收到RocketMQ的Half消息的响应，现在执行本地事务...");
        int number = (Integer) arg;
        try {
            Integer result = 100 / number;
            log.info("事务执行结果: {}", result);
            // 线程睡眠500毫秒模拟本地事务执行
            TimeUnit.MILLISECONDS.sleep(500);
            log.info("本地事务执行成功，给RocketMQ发送ACK响应");
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            log.info("本地事务执行发生异常，需要回滚事务");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        log.info("由于RocketMQ长时间无法收到消息的状态或本地执行事务状态为UNKNOW，现在执行补偿事务/回查本地事务...");
        return RocketMQLocalTransactionState.COMMIT;
    }

}
