package com.github.gelald.rocketmq.producer.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author WuYingBin
 * Date 2022/7/22
 */
@SpringBootApplication(scanBasePackages = {
        "com.github.gelald.rocketmq.common",
        "com.github.gelald.rocketmq.producer.starter"}
)
public class RocketMQProducerStarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketMQProducerStarterApplication.class, args);
    }
}
