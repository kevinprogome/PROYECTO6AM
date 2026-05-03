/*
 * Proyecto: GreenHouse Manager
 * Archivo: AlertaResponse.java
 * Descripcion: DTO de respuesta para alertas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.dto.response;

import com.greenhouse.manager.domain.enums.AlertaSeveridad;
import com.greenhouse.manager.domain.enums.AlertaTipo;
import java.time.LocalDateTime;

/**
 * Response DTO for Alerta entity.
 */
public class AlertaResponse {

    private Long id;
    private Long plantaId;
    private Long invernaderoId;
    private AlertaTipo tipo;
    private AlertaSeveridad severidad;
    private String mensaje;
    private Boolean activa;
    private LocalDateTime fechaGeneracion;
    private LocalDateTime fechaResolucion;
    private Long resueltaPorUsuarioId;

    /**
     * Gets the alert id.
     *
     * @return alert id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the alert id.
     *
     * @param id alert id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the plant id.
     *
     * @return plant id
     */
    public Long getPlantaId() {
        return plantaId;
    }

    /**
     * Sets the plant id.
     *
     * @param plantaId plant id
     */
    public void setPlantaId(Long plantaId) {
        this.plantaId = plantaId;
    }

    /**
     * Gets the greenhouse id.
     *
     * @return greenhouse id
     */
    public Long getInvernaderoId() {
        return invernaderoId;
    }

    /**
     * Sets the greenhouse id.
     *
     * @param invernaderoId greenhouse id
     */
    public void setInvernaderoId(Long invernaderoId) {
        this.invernaderoId = invernaderoId;
    }

    /**
     * Gets the alert type.
     *
     * @return alert type
     */
    public AlertaTipo getTipo() {
        return tipo;
    }

    /**
     * Sets the alert type.
     *
     * @param tipo alert type
     */
    public void setTipo(AlertaTipo tipo) {
        this.tipo = tipo;
    }

    /**
     * Gets the alert severity.
     *
     * @return alert severity
     */
    public AlertaSeveridad getSeveridad() {
        return severidad;
    }

    /**
     * Sets the alert severity.
     *
     * @param severidad alert severity
     */
    public void setSeveridad(AlertaSeveridad severidad) {
        this.severidad = severidad;
    }

    /**
     * Gets the message.
     *
     * @return message
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Sets the message.
     *
     * @param mensaje message
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Gets whether the alert is active.
     *
     * @return active flag
     */
    public Boolean getActiva() {
        return activa;
    }

    /**
     * Sets whether the alert is active.
     *
     * @param activa active flag
     */
    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    /**
     * Gets the generation timestamp.
     *
     * @return generation timestamp
     */
    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    /**
     * Sets the generation timestamp.
     *
     * @param fechaGeneracion generation timestamp
     */
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    /**
     * Gets the resolution timestamp.
     *
     * @return resolution timestamp
     */
    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    /**
     * Sets the resolution timestamp.
     *
     * @param fechaResolucion resolution timestamp
     */
    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    /**
     * Gets the resolving user id.
     *
     * @return resolving user id
     */
    public Long getResueltaPorUsuarioId() {
        return resueltaPorUsuarioId;
    }

    /**
     * Sets the resolving user id.
     *
     * @param resueltaPorUsuarioId resolving user id
     */
    public void setResueltaPorUsuarioId(Long resueltaPorUsuarioId) {
        this.resueltaPorUsuarioId = resueltaPorUsuarioId;
    }
}
