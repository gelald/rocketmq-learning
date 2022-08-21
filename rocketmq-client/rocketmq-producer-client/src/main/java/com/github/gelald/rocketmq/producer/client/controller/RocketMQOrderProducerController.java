package com.github.gelald.rocketmq.producer.client.controller;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RocketMQOrderProducerController {

    private DefaultMQProducer globalMQProducer;
    private DefaultMQProducer partitionedMQProducer;

    @ApiOperation("测试全局有序消息")
    @GetMapping("/global-order")
    public String sendGlobalOrderMessage() throws RemotingException, InterruptedException, MQClientException, MQBrokerException {
        for (int i = 1; i <= 20; i++) {
            String messageBody = "测试全局有序第" + i + "条消息";
            Message message = new Message((RocketMQConstant.TOPIC_PREFIX + "client-global-order"), messageBody.getBytes(StandardCharsets.UTF_8));
            message.putUserProperty("number", String.valueOf(i));
            this.globalMQProducer.send(message);
        }
        return "send complete";
    }

    @ApiOperation("测试分区有序消息")
    @GetMapping("/partitioned-order")
    public String sendPartitionedOrderMessage() throws RemotingException, InterruptedException, MQClientException, MQBrokerException {
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                String messageBody = "手机订单创建-" + i;
                Message message = new Message((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"), "phone-order", messageBody.getBytes(StandardCharsets.UTF_8));
                message.putUserProperty("number", String.valueOf(i));
                this.partitionedMQProducer.send(message, (messageQueueList, msg, arg) -> {
                    Integer id = (Integer) arg;
                    //使用取模算法确定id存放到哪个队列
                    //index就是要存放的队列的索引
                    int index = id % 2;
                    return messageQueueList.get(index);
                }, i);

                messageBody = "手机订单支付-" + i;
                message = new Message((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"), "phone-pay", messageBody.getBytes(StandardCharsets.UTF_8));
                message.putUserProperty("number", String.valueOf(i));
                this.partitionedMQProducer.send(message, (messageQueueList, msg, arg) -> {
                    Integer id = (Integer) arg;
                    int index = id % 2;
                    return messageQueueList.get(index);
                }, i);

                messageBody = "手机订单发货-" + i;
                message = new Message((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"), "phone-deliver", messageBody.getBytes(StandardCharsets.UTF_8));
                message.putUserProperty("number", String.valueOf(i));
                this.partitionedMQProducer.send(message, (messageQueueList, msg, arg) -> {
                    Integer id = (Integer) arg;
                    int index = id % 2;
                    return messageQueueList.get(index);
                }, i);
            } else {
                String messageBody = "衣服订单创建-" + i;
                Message message = new Message((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"), "clothes-order", messageBody.getBytes(StandardCharsets.UTF_8));
                message.putUserProperty("number", String.valueOf(i));
                this.partitionedMQProducer.send(message, (messageQueueList, msg, arg) -> {
                    Integer id = (Integer) arg;
                    int index = id % 2;
                    return messageQueueList.get(index);
                }, i);

                messageBody = "衣服订单支付-" + i;
                message = new Message((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"), "clothes-pay", messageBody.getBytes(StandardCharsets.UTF_8));
                message.putUserProperty("number", String.valueOf(i));
                this.partitionedMQProducer.send(message, (messageQueueList, msg, arg) -> {
                    Integer id = (Integer) arg;
                    int index = id % 2;
                    return messageQueueList.get(index);
                }, i);

                messageBody = "衣服订单发货-" + i;
                message = new Message((RocketMQConstant.TOPIC_PREFIX + "client-partitioned-order"), "clothes-deliver", messageBody.getBytes(StandardCharsets.UTF_8));
                message.putUserProperty("number", String.valueOf(i));
                this.partitionedMQProducer.send(message, (messageQueueList, msg, arg) -> {
                    Integer id = (Integer) arg;
                    int index = id % 2;
                    return messageQueueList.get(index);
                }, i);
            }
        }
        return "send complete";
    }

    @Autowired
    public void setDefaultMQProducer(DefaultMQProducer globalMQProducer) {
        this.globalMQProducer = globalMQProducer;
    }

    @Autowired
    public void setPartitionedMQProducer(DefaultMQProducer partitionedMQProducer) {
        this.partitionedMQProducer = partitionedMQProducer;
    }
}
