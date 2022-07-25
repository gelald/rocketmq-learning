package com.github.gelald.rocketmq.common.result;

/**
 * @author WuYingBin
 * date: 2022/7/13
 */
public enum ResultEnum implements IResult {
    SUCCESS(2001, "接口调用成功"),
    VALIDATE_FAILED(2002, "参数校验失败"),
    COMMON_FAILED(2003, "接口调用失败"),
    UNAUTHORIZED(2004, "未登录或Token已过期"),
    FORBIDDEN(2005, "没有权限访问资源");

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
