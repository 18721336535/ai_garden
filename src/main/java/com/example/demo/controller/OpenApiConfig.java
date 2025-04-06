package com.example.demo.controller;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot 3.4 Swagger 示例")
                        .version("1.0.0")
                        .description("这是一个简单的Spring Boot 3.4项目，使用Springdoc OpenAPI集成Swagger。"));
    }
}

