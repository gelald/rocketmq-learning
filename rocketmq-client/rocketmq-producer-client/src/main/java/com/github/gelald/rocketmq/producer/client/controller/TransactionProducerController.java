package com.github.gelald.rocketmq.producer.client.controller;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @author WuYingBin
 * date: 2022/8/22
 */
@Slf4j
@RestController
@Api(tags = "事务消息生产者")
@RequestMapping("/transaction-producer")
public class TransactionProducerController {
    private TransactionMQProducer transactionMQProducer;

    @ApiOperation("发送事务消息")
    @GetMapping("/{number}")
    public String sendTransactionMessage(@PathVariable Integer number) throws MQClientException {
        log.info("接收到事务请求，准备执行生产者本地事务...");
        Message message = new Message((RocketMQConstant.TOPIC_PREFIX + "client-transaction"), "通知消费者执行本地事务的事务消息".getBytes(StandardCharsets.UTF_8));
        message.putUserProperty("number", number.toString());
        this.transactionMQProducer.sendMessageInTransaction(message, null);
        log.info("事务型生产者发送消息: {}", message);
        return "事务消息发送成功";
    }

    @Autowired(required = false)
    @Qualifier("transactionMQProducer")
    public void setTransactionMQProducer(TransactionMQProducer transactionMQProducer) {
        this.transactionMQProducer = transactionMQProducer;
    }
}
