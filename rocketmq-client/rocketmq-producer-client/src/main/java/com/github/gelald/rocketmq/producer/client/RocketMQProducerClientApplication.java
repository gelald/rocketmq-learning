package com.github.gelald.rocketmq.producer.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author WuYingBin
 * Date 2022/7/26
 */
@SpringBootApplication(scanBasePackages = {
        "com.github.gelald.rocketmq.producer.client",
        "com.github.gelald.rocketmq.common"
})
public class RocketMQProducerClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketMQProducerClientApplication.class, args);
    }
}
