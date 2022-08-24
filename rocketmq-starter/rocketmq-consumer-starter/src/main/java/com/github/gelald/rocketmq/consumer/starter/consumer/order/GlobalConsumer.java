package com.github.gelald.rocketmq.consumer.starter.consumer.order;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author WuYingBin
 * Date 2022/8/24
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = (RocketMQConstant.CONSUMER_GROUP_PREFIX + "starter-global-order"),
        topic = (RocketMQConstant.TOPIC_PREFIX + "starter-global-order"),
        consumeMode = ConsumeMode.ORDERLY
)
public class GlobalConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("GlobalConsumer接收到消息, 消息内容: {}", message);
    }
}
