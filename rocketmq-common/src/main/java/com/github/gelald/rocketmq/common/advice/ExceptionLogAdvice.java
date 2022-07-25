package com.github.gelald.rocketmq.common.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author WuYingBin
 * date: 2022/7/24
 */
@Aspect
@Component
public class ExceptionLogAdvice {
    //基于异常拦截器注解，更准确
    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public void exceptionHandler() {

    }

    @Before(value = "exceptionHandler()")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args[0] instanceof Exception) {
            Exception exception = (Exception) args[0];
            exception.printStackTrace();
        }
    }
}
