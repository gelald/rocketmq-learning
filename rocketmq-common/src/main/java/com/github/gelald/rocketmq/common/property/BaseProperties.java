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
    private String responseBodyAdvicePackage;
    private String swaggerPackage;
    private String swaggerTitle;

    public void setResponseBodyAdvicePackage(String responseBodyAdvicePackage) {
        this.responseBodyAdvicePackage = responseBodyAdvicePackage;
    }

    public void setSwaggerPackage(String swaggerPackage) {
        this.swaggerPackage = swaggerPackage;
    }

    public void setSwaggerTitle(String swaggerTitle) {
        this.swaggerTitle = swaggerTitle;
    }
}
