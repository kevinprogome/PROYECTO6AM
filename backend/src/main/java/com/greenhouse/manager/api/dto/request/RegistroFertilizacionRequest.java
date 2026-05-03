/*
 * Proyecto: GreenHouse Manager
 * Archivo: RegistroFertilizacionRequest.java
 * Descripcion: DTO de solicitud para registros de fertilizacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request DTO for RegistroFertilizacion entity.
 */
public class RegistroFertilizacionRequest {

    @NotNull
    private Long plantaId;

    @NotNull
    private LocalDateTime fechaFertilizacion;

    @NotBlank
    @Size(max = 120)
    private String tipoFertilizante;

    private BigDecimal dosis;

    @Size(max = 20)
    private String unidad;

    @Size(max = 120)
    private String responsable;

    @Size(max = 300)
    private String notas;

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
}
