/*
 * Proyecto: GreenHouse Manager
 * Archivo: CorsConfig.java
 * Descripcion: Configuracion CORS para el frontend.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-03
 * Version: 1.0.0
 */
package com.greenhouse.manager.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures allowed origins for CORS.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final List<String> allowedOrigins;

    /**
     * Creates a new CorsConfig.
     *
     * @param allowedOrigins allowed origins list
     */
    public CorsConfig(
        @Value("${app.cors.allowed-origins:http://localhost:5173}") String allowedOrigins
    ) {
        this.allowedOrigins = Arrays.stream(allowedOrigins.split(","))
            .map(String::trim)
            .filter(value -> !value.isEmpty())
            .collect(Collectors.toList());
    }

    /**
     * Configures CORS mappings.
     *
     * @param registry cors registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(allowedOrigins.toArray(new String[0]))
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
