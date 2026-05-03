/*
 * Proyecto: GreenHouse Manager
 * Archivo: RegistroFertilizacionResponse.java
 * Descripcion: DTO de respuesta para registros de fertilizacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for RegistroFertilizacion entity.
 */
public class RegistroFertilizacionResponse {

    private Long id;
    private Long plantaId;
    private LocalDateTime fechaFertilizacion;
    private String tipoFertilizante;
    private BigDecimal dosis;
    private String unidad;
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
     * Gets the fertilization date.
     *
     * @return fertilization date
     */
    public LocalDateTime getFechaFertilizacion() {
        return fechaFertilizacion;
    }

    /**
     * Sets the fertilization date.
     *
     * @param fechaFertilizacion fertilization date
     */
    public void setFechaFertilizacion(LocalDateTime fechaFertilizacion) {
        this.fechaFertilizacion = fechaFertilizacion;
    }

    /**
     * Gets the fertilizer type.
     *
     * @return fertilizer type
     */
    public String getTipoFertilizante() {
        return tipoFertilizante;
    }

    /**
     * Sets the fertilizer type.
     *
     * @param tipoFertilizante fertilizer type
     */
    public void setTipoFertilizante(String tipoFertilizante) {
        this.tipoFertilizante = tipoFertilizante;
    }

    /**
     * Gets the dose.
     *
     * @return dose
     */
    public BigDecimal getDosis() {
        return dosis;
    }

    /**
     * Sets the dose.
     *
     * @param dosis dose
     */
    public void setDosis(BigDecimal dosis) {
        this.dosis = dosis;
    }

    /**
     * Gets the unit of measure.
     *
     * @return unit of measure
     */
    public String getUnidad() {
        return unidad;
    }

    /**
     * Sets the unit of measure.
     *
     * @param unidad unit of measure
     */
    public void setUnidad(String unidad) {
        this.unidad = unidad;
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
