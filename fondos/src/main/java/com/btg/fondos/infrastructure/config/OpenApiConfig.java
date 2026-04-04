package com.btg.fondos.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Fondos - BTG Pactual")
                        .version("1.0")
                        .description("Documentación de la API REST para la prueba técnica de Backend.")
                        .contact(new Contact()
                                .name("Jhon Javier Cardona Muñoz")
                                .email("jhonjcm2@egmail.com")));
    }
}