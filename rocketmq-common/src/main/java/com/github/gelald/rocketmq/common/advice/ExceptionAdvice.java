package com.github.gelald.rocketmq.common.advice;

import com.github.gelald.rocketmq.common.exception.BusinessException;
import com.github.gelald.rocketmq.common.exception.ForbiddenException;
import com.github.gelald.rocketmq.common.result.Result;
import com.github.gelald.rocketmq.common.result.ResultEnum;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @author WuYingBin
 * date: 2022/7/13
 */
@RestControllerAdvice(basePackages = "${base.responseBodyAdvicePackage}")
public class ExceptionAdvice {

    /**
     * 捕获 {@code BusinessException} 异常
     */
    @ExceptionHandler({BusinessException.class})
    public Result<?> handleBusinessException(BusinessException ex) {
        return Result.failed(ex.getMessage());
    }

    /**
     * 捕获 {@code ForbiddenException} 异常
     */
    @ExceptionHandler({ForbiddenException.class})
    public Result<?> handleForbiddenException(ForbiddenException ex) {
        return Result.failed(ResultEnum.FORBIDDEN);
    }

    /**
     * {@code @RequestBody} 参数校验不通过时抛出的异常处理
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        if (StringUtils.hasText(msg)) {
            return Result.failed(ResultEnum.VALIDATE_FAILED.getCode(), msg);
        }
        return Result.failed(ResultEnum.VALIDATE_FAILED);
    }

    /**
     * {@code @PathVariable} 和 {@code @RequestParam} 参数校验不通过时抛出的异常处理
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public Result<?> handleConstraintViolationException(ConstraintViolationException ex) {
        if (StringUtils.hasText(ex.getMessage())) {
            return Result.failed(ResultEnum.VALIDATE_FAILED.getCode(), ex.getMessage());
        }
        return Result.failed(ResultEnum.VALIDATE_FAILED);
    }

    /**
     * 顶级异常捕获并统一处理，当其他异常无法处理时候选择使用
     */
    @ExceptionHandler({Exception.class})
    public Result<?> handle(Exception ex) {
        return Result.failed(ex.getMessage());
    }

}
