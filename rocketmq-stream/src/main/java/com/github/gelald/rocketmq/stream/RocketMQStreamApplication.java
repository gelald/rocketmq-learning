package com.github.gelald.rocketmq.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;

/**
 * @author WuYingBin
 * Date 2022/7/22
 */
@SpringBootApplication
@EnableBinding({Source.class, Sink.class})
public class RocketMQStreamApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketMQStreamApplication.class, args);
    }
}
