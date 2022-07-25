package com.github.gelald.rocketmq.common.exception;

/**
 * @author WuYingBin
 * date: 2022/7/13
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
