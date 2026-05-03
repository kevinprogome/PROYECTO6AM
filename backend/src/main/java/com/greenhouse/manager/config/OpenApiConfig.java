/*
 * Proyecto: GreenHouse Manager
 * Archivo: OpenApiConfig.java
 * Descripcion: Configuracion de Swagger/OpenAPI.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for API documentation.
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "GreenHouse Manager API",
        version = "1.0.0",
        description = "API REST para la gestion de cultivos en invernaderos.",
        contact = @Contact(name = "Equipo GreenHouse Manager", email = "equipo@greenhouse.local"),
        license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT")
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Servidor local"),
        @Server(url = "https://api.greenhouse.local", description = "Servidor produccion")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // Configuration via annotations.
}
