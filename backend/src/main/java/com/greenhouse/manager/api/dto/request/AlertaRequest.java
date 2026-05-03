/*
 * Proyecto: GreenHouse Manager
 * Archivo: AlertaRequest.java
 * Descripcion: DTO de solicitud para alertas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.dto.request;

import com.greenhouse.manager.domain.enums.AlertaSeveridad;
import com.greenhouse.manager.domain.enums.AlertaTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Request DTO for Alerta entity.
 */
public class AlertaRequest {

    @NotNull
    private Long plantaId;

    @NotNull
    private Long invernaderoId;

    @NotNull
    private AlertaTipo tipo;

    @NotNull
    private AlertaSeveridad severidad;

    @NotBlank
    @Size(max = 300)
    private String mensaje;

    @NotNull
    private Boolean activa;

    private LocalDateTime fechaGeneracion;

    private LocalDateTime fechaResolucion;

    private Long resueltaPorUsuarioId;

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
