package com.github.gelald.rocketmq.common.knife4j;

import com.github.gelald.rocketmq.common.property.BaseProperties;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author WuYingBin
 * date: 2022/7/23
 */
public abstract class BaseSwaggerConfiguration {
    @Bean
    public Docket createRestApi() {
        BaseProperties baseProperties = properties();
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo(baseProperties))
                .select()
                .apis(RequestHandlerSelectors.basePackage(baseProperties.getSwaggerPackage()))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(BaseProperties baseProperties) {
        return new ApiInfoBuilder()
                .title(baseProperties.getSwaggerTitle())
                .description("暂无描述")
                .contact(new Contact("gelald", "https://gelald.github.io/javrin/", "yb.ng@foxmail.com"))
                .version("1.0.0")
                .build();
    }

    public abstract BaseProperties properties();
}
