package com.oxysystem.general.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.GroupedOpenApi;

import java.util.Collections;
import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "OXYSYSTEM API",
                version = "1.0",
                description = "Oxysystem api protected with Bearer Token",
                contact = @Contact(
                        name = "Oxysystem Support",
                        email = "info@oxysystem.com",
                        url = "https://www.oxysystem.com/"
                )
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "bearerAuth",
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
    @Value("${app.openapi.server-url}")
    private String serverUrl;

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/api/**","/rest/auth/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(Collections.singletonList(
                        new Server()
                                .url(serverUrl)
                                .description("Current Environment Server")
                ));
    }
}
