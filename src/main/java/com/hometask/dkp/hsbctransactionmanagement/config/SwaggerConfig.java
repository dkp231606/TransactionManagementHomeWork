package com.hometask.dkp.hsbctransactionmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: dkp
 * @date: 2025-03-09
 */

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Transaction System API Documentation")
                        .description("Transaction Management API Documentation")
                        .version("1.0.0"));
    }
}
