package com.cassi.desafiocassi.configuration.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Desafio Cassi API",
        version = "${api.rest.version}",
        description = "API de cadastro de produtos"
    )
)
public class SwaggerConfig {
}