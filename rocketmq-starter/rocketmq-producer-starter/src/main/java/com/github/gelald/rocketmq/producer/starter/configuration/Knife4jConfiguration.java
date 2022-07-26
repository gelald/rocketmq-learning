package com.github.gelald.rocketmq.producer.starter.configuration;

import com.github.gelald.rocketmq.common.knife4j.BaseSwaggerConfiguration;
import com.github.gelald.rocketmq.common.property.BaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author WuYingBin
 * date: 2022/7/27
 */
@Configuration
public class Knife4jConfiguration extends BaseSwaggerConfiguration {
    private BaseProperties baseProperties;

    @Override
    public BaseProperties properties() {
        return this.baseProperties;
    }

    @Autowired
    public void setBaseProperties(BaseProperties baseProperties) {
        this.baseProperties = baseProperties;
    }
}
