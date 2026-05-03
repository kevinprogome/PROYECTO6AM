/*
 * Proyecto: GreenHouse Manager
 * Archivo: GlobalExceptionHandler.java
 * Descripcion: Manejador global de errores para la API.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.i18n.LocaleContextHolder; // ✅ CORRECTO
import org.springframework.http.converter.HttpMessageNotReadableException;

/**
 * Global exception handler for REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Creates a new GlobalExceptionHandler.
     *
     * @param messageSource message source for i18n
     */
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Handles resource not found errors.
     *
     * @param ex exception
     * @param request http request
     * @return error response
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
        NotFoundException ex,
        HttpServletRequest request
    ) {
        String message = resolveMessage(ex.getMessageKey(), ex.getArgs());
        ApiErrorResponse response = buildError(HttpStatus.NOT_FOUND, message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles validation errors on request bodies.
     *
     * @param ex exception
     * @param request http request
     * @return error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        List<String> details = new ArrayList<>();
        Locale locale = LocaleContextHolder.getLocale();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(resolveFieldError(error, locale));
        }
        String message = resolveMessage("error.validacion", null);
        ApiErrorResponse response = buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
        response.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles constraint violations from request parameters.
     *
     * @param ex exception
     * @param request http request
     * @return error response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
        ConstraintViolationException ex,
        HttpServletRequest request
    ) {
        List<String> details = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation -> details.add(violation.getMessage()));
        String message = resolveMessage("error.validacion", null);
        ApiErrorResponse response = buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
        response.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles malformed JSON payloads.
     *
     * @param ex exception
     * @param request http request
     * @return error response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpServletRequest request
    ) {
        String message = resolveMessage("error.request.invalida", null);
        ApiErrorResponse response = buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles unexpected errors.
     *
     * @param ex exception
     * @param request http request
     * @return error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
        Exception ex,
        HttpServletRequest request
    ) {
        String message = resolveMessage("error.interno", null);
        ApiErrorResponse response = buildError(HttpStatus.INTERNAL_SERVER_ERROR, message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private ApiErrorResponse buildError(HttpStatus status, String message, String path) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(status.value());
        response.setError(status.name());
        response.setMessage(message);
        response.setPath(path);
        return response;
    }

    private String resolveMessage(String key, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, key, locale);
    }

    private String resolveFieldError(FieldError error, Locale locale) {
        String code = error.getDefaultMessage();
        if (code == null) {
            return messageSource.getMessage("error.validacion", null, "error.validacion", locale);
        }
        if (code.startsWith("{") && code.endsWith("}")) {
            code = code.substring(1, code.length() - 1);
        }
        return messageSource.getMessage(code, error.getArguments(), code, locale);
    }
}
