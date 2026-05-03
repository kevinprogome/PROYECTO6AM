/*
 * Proyecto: GreenHouse Manager
 * Archivo: RegistroRiegoResponse.java
 * Descripcion: DTO de respuesta para registros de riego.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for RegistroRiego entity.
 */
public class RegistroRiegoResponse {

    private Long id;
    private Long plantaId;
    private LocalDateTime fechaRiego;
    private BigDecimal volumenLitros;
    private String metodo;
    private String responsable;
    private String notas;
    private LocalDateTime createdAt;

    /**
     * Gets the record id.
     *
     * @return record id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the record id.
     *
     * @param id record id
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
     * Gets the irrigation date.
     *
     * @return irrigation date
     */
    public LocalDateTime getFechaRiego() {
        return fechaRiego;
    }

    /**
     * Sets the irrigation date.
     *
     * @param fechaRiego irrigation date
     */
    public void setFechaRiego(LocalDateTime fechaRiego) {
        this.fechaRiego = fechaRiego;
    }

    /**
     * Gets the irrigation volume.
     *
     * @return irrigation volume
     */
    public BigDecimal getVolumenLitros() {
        return volumenLitros;
    }

    /**
     * Sets the irrigation volume.
     *
     * @param volumenLitros irrigation volume
     */
    public void setVolumenLitros(BigDecimal volumenLitros) {
        this.volumenLitros = volumenLitros;
    }

    /**
     * Gets the irrigation method.
     *
     * @return irrigation method
     */
    public String getMetodo() {
        return metodo;
    }

    /**
     * Sets the irrigation method.
     *
     * @param metodo irrigation method
     */
    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    /**
     * Gets the responsible operator.
     *
     * @return responsible operator
     */
    public String getResponsable() {
        return responsable;
    }

    /**
     * Sets the responsible operator.
     *
     * @param responsable responsible operator
     */
    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    /**
     * Gets the notes.
     *
     * @return notes
     */
    public String getNotas() {
        return notas;
    }

    /**
     * Sets the notes.
     *
     * @param notas notes
     */
    public void setNotas(String notas) {
        this.notas = notas;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return created at timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt created at timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
