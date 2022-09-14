package com.github.gelald.rocketmq.common.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author WuYingBin
 * date: 2022/7/25
 */
@Data
@Component
@ConfigurationProperties(prefix = "base")
public class BaseProperties {
    /**
     * 需要生成Swagger文档的包路径
     */
    private String swaggerPackage;
    /**
     * Swagger文档标题
     */
    private String swaggerTitle;

    public void setSwaggerPackage(String swaggerPackage) {
        this.swaggerPackage = swaggerPackage;
    }

    public void setSwaggerTitle(String swaggerTitle) {
        this.swaggerTitle = swaggerTitle;
    }
}
