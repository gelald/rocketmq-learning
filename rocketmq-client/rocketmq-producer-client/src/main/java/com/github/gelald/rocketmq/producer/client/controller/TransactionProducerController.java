package com.github.gelald.rocketmq.producer.client.controller;

import com.github.gelald.rocketmq.common.constant.RocketMQConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.apis.producer.Transaction;
import org.apache.rocketmq.client.java.message.MessageBuilderImpl;
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
@AllArgsConstructor
public class TransactionProducerController {

    // 普通生产者有指定 transaction checker，可以用来发送事务消息
    private final Producer producer;

    @ApiOperation("发送事务消息")
    @GetMapping("/{number}")
    public String sendTransactionMessage(@PathVariable Integer number) throws ClientException {
        log.info("接收到事务请求，准备执行生产者本地事务...");
        Transaction transaction = producer.beginTransaction();
        Message message = new MessageBuilderImpl()
                .setTopic((RocketMQConstant.TOPIC_PREFIX + "client-transaction"))
                .setBody("通知消费者执行本地事务的事务消息".getBytes(StandardCharsets.UTF_8))
                .build();
        try {
            SendReceipt sendReceipt = producer.send(message);
            int a = 10 / number;
            transaction.commit();
            log.info("本地事务执行成功, 事务 commit");
        } catch (Exception e) {
            transaction.rollback();
            log.info("本地事务执行失败, 事务 rollback");
        }
        return "事务消息发送成功";
    }
}
