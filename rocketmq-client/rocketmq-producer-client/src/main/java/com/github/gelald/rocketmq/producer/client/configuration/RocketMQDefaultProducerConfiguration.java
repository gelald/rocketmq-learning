package com.github.gelald.rocketmq.producer.client.configuration;

import com.github.gelald.rocketmq.producer.client.property.RocketMQProducerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.TransactionResolution;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@Slf4j
@Configuration
public class RocketMQDefaultProducerConfiguration extends RocketMQBaseProducerConfiguration {
    public RocketMQDefaultProducerConfiguration(RocketMQProducerProperties rocketMQProducerProperties) {
        super(rocketMQProducerProperties);
    }

    @Bean
    public Producer defaultMQProducer() throws ClientException {
        // 创建消息生产者
        Producer defaultMQProducer = provider.newProducerBuilder()
                // setTopics的作用是预注册 topic，让客户端在启动时预先获取路由元数据
                // 创建时一旦设置，那么发送的消息 topic 只能从这里选择，不能发送新的 topic
                // 并且可以快速失败检测，不用等到发送消息时才报错
                // 可以不设置，保持灵活，全局复用同一个 producer
                // .setTopics()
                .setClientConfiguration(configuration)
                // 设置事务回查逻辑，即使定义了也可以发送普通消息
                // 事务提交或回滚放到了调用方
                // 这里的逻辑是事务未及时commit或rollback时broker执行的回查
                .setTransactionChecker(messageView -> {
                    String messageId = messageView.getMessageId().toString();
                    log.info("由于RocketMQ长时间无法收到消息: {} 的状态或本地执行事务状态为UNKNOW，现在执行补偿事务/回查本地事务...", messageId);
                    return TransactionResolution.COMMIT;
                })
                .build();
        // 把创建的生产者放到一个集合，当程序结束时统一销毁
        mqProducers.add(defaultMQProducer);
        return defaultMQProducer;
    }
}
