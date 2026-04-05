package com.banking.cbs.customer.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customerEntityOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer & Entity Management API")
                        .description("Core Banking System — Customer and Entity Management Service.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("BankSoft")
                                .email("admin@banksoft.com"))
                        .license(new License()
                                .name("Internal Use Only")))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Local Development")
                ));
    }
}
