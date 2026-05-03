/*
 * Proyecto: GreenHouse Manager
 * Archivo: RestAuthenticationEntryPoint.java
 * Descripcion: Entrada de autenticacion para APIs protegidas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenhouse.manager.api.exception.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.context.i18n.LocaleContextHolder; // ✅ CORRECTO
/**
 * Authentication entry point that returns JSON responses.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;

    /**
     * Creates a new RestAuthenticationEntryPoint.
     *
     * @param messageSource message source
     * @param objectMapper object mapper
     */
    public RestAuthenticationEntryPoint(MessageSource messageSource, ObjectMapper objectMapper) {
        this.messageSource = messageSource;
        this.objectMapper = objectMapper;
    }

    /**
     * Handles authentication failures.
     *
     * @param request http request
     * @param response http response
     * @param authException authentication exception
     * @throws IOException on io errors
     */
    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage("error.unauthorized", null, "error.unauthorized", locale);

        ApiErrorResponse error = new ApiErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setError(HttpStatus.UNAUTHORIZED.name());
        error.setMessage(message);
        error.setPath(request.getRequestURI());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), error);
    }
}
