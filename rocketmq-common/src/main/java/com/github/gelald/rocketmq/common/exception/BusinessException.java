package com.github.gelald.rocketmq.common.exception;

/**
 * @author WuYingBin
 * date: 2022/7/13
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
