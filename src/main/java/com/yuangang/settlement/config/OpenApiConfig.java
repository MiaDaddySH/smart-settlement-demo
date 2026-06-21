package com.yuangang.settlement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI smartSettlementOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Settlement Demo API")
                        .version("0.1")
                        .description("Invoice settlement, payment simulation, and audit tracking API."));
    }
}
