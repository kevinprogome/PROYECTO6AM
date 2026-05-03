/*
 * Proyecto: GreenHouse Manager
 * Archivo: ApiErrorResponse.java
 * Descripcion: DTO para respuestas de error.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response payload for API errors.
 */
public class ApiErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> details;

    /**
     * Gets the error timestamp.
     *
     * @return timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the error timestamp.
     *
     * @param timestamp timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the HTTP status code.
     *
     * @return status code
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the HTTP status code.
     *
     * @param status status code
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets the error code.
     *
     * @return error code
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error code.
     *
     * @param error error code
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Gets the localized message.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the localized message.
     *
     * @param message message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the request path.
     *
     * @return request path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the request path.
     *
     * @param path request path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets the validation details.
     *
     * @return details list
     */
    public List<String> getDetails() {
        return details;
    }

    /**
     * Sets the validation details.
     *
     * @param details details list
     */
    public void setDetails(List<String> details) {
        this.details = details;
    }
}
