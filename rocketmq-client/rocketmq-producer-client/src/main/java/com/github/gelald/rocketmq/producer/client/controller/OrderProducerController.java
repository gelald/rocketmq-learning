package com.github.gelald.rocketmq.producer.client.controller;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.java.message.MessageBuilderImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @author WuYingBin
 * date: 2022/8/20
 */
@Slf4j
@RestController
@Api(tags = "顺序消息生产者")
@RequestMapping("/order-producer")
@AllArgsConstructor
public class OrderProducerController {

    // 用普通生产者发送即可，在消息处通过控制messageGroup来控制有序
    private final Producer defaultMQProducer;

    @ApiOperation("测试全局有序消息")
    @GetMapping("/global-order")
    public String sendGlobalOrderMessage() throws ClientException {
        for (int i = 1; i <= 20; i++) {
            String messageBody = "测试全局有序第" + i + "条消息";
            Message message = new MessageBuilderImpl()
                    .setTopic((RocketMQConstant.TOPIC_PREFIX + "client-global-order"))
                    .setTag("number" + i)
                    .setBody(messageBody.getBytes(StandardCharsets.UTF_8))
                    .setMessageGroup("global")
                    .build();
            this.defaultMQProducer.send(message);
        }
        return "send complete";
    }

    @ApiOperation("测试分区有序消息")
    @GetMapping("/partitioned-order")
    public String sendPartitionedOrderMessage() throws ClientException {
        // 目标：订单创建、订单支付、订单发货必须按顺序，但是不同单品可以并发（不同队列）
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                String messageBody = "手机订单创建-" + i;
                Message message = new MessageBuilderImpl()
                        .setTopic((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"))
                        .setTag("phone-order" + i)
                        .setBody(messageBody.getBytes(StandardCharsets.UTF_8))
                        .setMessageGroup("phone")
                        .build();
                this.defaultMQProducer.send(message);

                messageBody = "手机订单支付-" + i;
                message = new MessageBuilderImpl()
                        .setTopic((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"))
                        .setTag("phone-pay" + i)
                        .setBody(messageBody.getBytes(StandardCharsets.UTF_8))
                        .setMessageGroup("phone")
                        .build();
                this.defaultMQProducer.send(message);

                messageBody = "手机订单发货-" + i;
                message = new MessageBuilderImpl()
                        .setTopic((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"))
                        .setTag("phone-deliver" + i)
                        .setBody(messageBody.getBytes(StandardCharsets.UTF_8))
                        .setMessageGroup("phone")
                        .build();
                this.defaultMQProducer.send(message);
            } else {
                String messageBody = "衣服订单创建-" + i;
                Message message = new MessageBuilderImpl()
                        .setTopic((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"))
                        .setTag("clothes-order" + i)
                        .setBody(messageBody.getBytes(StandardCharsets.UTF_8))
                        .setMessageGroup("clothes")
                        .build();
                this.defaultMQProducer.send(message);

                messageBody = "衣服订单支付-" + i;
                message = new MessageBuilderImpl()
                        .setTopic((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"))
                        .setTag("clothes-pay" + i)
                        .setBody(messageBody.getBytes(StandardCharsets.UTF_8))
                        .setMessageGroup("clothes")
                        .build();
                this.defaultMQProducer.send(message);

                messageBody = "衣服订单发货-" + i;
                message = new MessageBuilderImpl()
                        .setTopic((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"))
                        .setTag("clothes-deliver" + i)
                        .setBody(messageBody.getBytes(StandardCharsets.UTF_8))
                        .setMessageGroup("clothes")
                        .build();
                this.defaultMQProducer.send(message);
            }
        }
        return "send complete";
    }
}
