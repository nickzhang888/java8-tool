package com.nick.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger3 配置，启动后访问：http://localhost:8000/swagger-ui/index.html
 * OpenAPI JSON：http://localhost:8000/v3/api-docs （可导入 Postman）
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.nick.api.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("javaTool API 文档")
                .description("本地接口测试文档，可在页面直接调试；也可将 /v3/api-docs 导入 Postman")
                .contact(new Contact("nick", "", ""))
                .version("1.0.0")
                .build();
    }
}
