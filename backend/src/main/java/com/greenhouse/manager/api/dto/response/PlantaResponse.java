/*
 * Proyecto: GreenHouse Manager
 * Archivo: PlantaResponse.java
 * Descripcion: DTO de respuesta para plantas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.dto.response;

import com.greenhouse.manager.domain.enums.EstadoCultivoStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for Planta entity.
 */
public class PlantaResponse {

    private Long id;
    private Long invernaderoId;
    private String nombreComun;
    private String nombreCientifico;
    private String variedad;
    private LocalDate fechaSiembra;
    private LocalDate fechaUltimoRiego;
    private Integer frecuenciaRiegoDias;
    private LocalDate fechaUltimaFertilizacion;
    private Integer frecuenciaFertilizacionDias;
    private EstadoCultivoStatus estadoActual;
    private String observaciones;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Gets the plant id.
     *
     * @return plant id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the plant id.
     *
     * @param id plant id
     */
    public void setId(Long id) {
        this.id = id;
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
     * Gets the common name.
     *
     * @return common name
     */
    public String getNombreComun() {
        return nombreComun;
    }

    /**
     * Sets the common name.
     *
     * @param nombreComun common name
     */
    public void setNombreComun(String nombreComun) {
        this.nombreComun = nombreComun;
    }

    /**
     * Gets the scientific name.
     *
     * @return scientific name
     */
    public String getNombreCientifico() {
        return nombreCientifico;
    }

    /**
     * Sets the scientific name.
     *
     * @param nombreCientifico scientific name
     */
    public void setNombreCientifico(String nombreCientifico) {
        this.nombreCientifico = nombreCientifico;
    }

    /**
     * Gets the variety.
     *
     * @return variety
     */
    public String getVariedad() {
        return variedad;
    }

    /**
     * Sets the variety.
     *
     * @param variedad variety
     */
    public void setVariedad(String variedad) {
        this.variedad = variedad;
    }

    /**
     * Gets the sowing date.
     *
     * @return sowing date
     */
    public LocalDate getFechaSiembra() {
        return fechaSiembra;
    }

    /**
     * Sets the sowing date.
     *
     * @param fechaSiembra sowing date
     */
    public void setFechaSiembra(LocalDate fechaSiembra) {
        this.fechaSiembra = fechaSiembra;
    }

    /**
     * Gets the last irrigation date.
     *
     * @return last irrigation date
     */
    public LocalDate getFechaUltimoRiego() {
        return fechaUltimoRiego;
    }

    /**
     * Sets the last irrigation date.
     *
     * @param fechaUltimoRiego last irrigation date
     */
    public void setFechaUltimoRiego(LocalDate fechaUltimoRiego) {
        this.fechaUltimoRiego = fechaUltimoRiego;
    }

    /**
     * Gets the irrigation frequency in days.
     *
     * @return irrigation frequency
     */
    public Integer getFrecuenciaRiegoDias() {
        return frecuenciaRiegoDias;
    }

    /**
     * Sets the irrigation frequency in days.
     *
     * @param frecuenciaRiegoDias irrigation frequency
     */
    public void setFrecuenciaRiegoDias(Integer frecuenciaRiegoDias) {
        this.frecuenciaRiegoDias = frecuenciaRiegoDias;
    }

    /**
     * Gets the last fertilization date.
     *
     * @return last fertilization date
     */
    public LocalDate getFechaUltimaFertilizacion() {
        return fechaUltimaFertilizacion;
    }

    /**
     * Sets the last fertilization date.
     *
     * @param fechaUltimaFertilizacion last fertilization date
     */
    public void setFechaUltimaFertilizacion(LocalDate fechaUltimaFertilizacion) {
        this.fechaUltimaFertilizacion = fechaUltimaFertilizacion;
    }

    /**
     * Gets the fertilization frequency in days.
     *
     * @return fertilization frequency
     */
    public Integer getFrecuenciaFertilizacionDias() {
        return frecuenciaFertilizacionDias;
    }

    /**
     * Sets the fertilization frequency in days.
     *
     * @param frecuenciaFertilizacionDias fertilization frequency
     */
    public void setFrecuenciaFertilizacionDias(Integer frecuenciaFertilizacionDias) {
        this.frecuenciaFertilizacionDias = frecuenciaFertilizacionDias;
    }

    /**
     * Gets the current status.
     *
     * @return current status
     */
    public EstadoCultivoStatus getEstadoActual() {
        return estadoActual;
    }

    /**
     * Sets the current status.
     *
     * @param estadoActual current status
     */
    public void setEstadoActual(EstadoCultivoStatus estadoActual) {
        this.estadoActual = estadoActual;
    }

    /**
     * Gets the notes.
     *
     * @return notes
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Sets the notes.
     *
     * @param observaciones notes
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Gets whether the plant is active.
     *
     * @return active flag
     */
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Sets whether the plant is active.
     *
     * @param activo active flag
     */
    public void setActivo(Boolean activo) {
        this.activo = activo;
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

    /**
     * Gets the update timestamp.
     *
     * @return updated at timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the update timestamp.
     *
     * @param updatedAt updated at timestamp
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
