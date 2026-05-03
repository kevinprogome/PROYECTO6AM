/*
 * Proyecto: GreenHouse Manager
 * Archivo: OpenApiSecurity.java
 * Descripcion: Anotacion para requerir seguridad en OpenAPI.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.config;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation to apply JWT security requirement.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@SecurityRequirement(name = "bearerAuth")
public @interface OpenApiSecurity {
    // Marker annotation for Swagger security.
}