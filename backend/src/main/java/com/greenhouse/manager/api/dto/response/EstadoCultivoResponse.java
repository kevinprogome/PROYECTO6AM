/*
 * Proyecto: GreenHouse Manager
 * Archivo: EstadoCultivoResponse.java
 * Descripcion: DTO de respuesta para estados de cultivo.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.dto.response;

import com.greenhouse.manager.domain.enums.EstadoCultivoStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for EstadoCultivo entity.
 */
public class EstadoCultivoResponse {

    private Long id;
    private Long plantaId;
    private LocalDateTime fechaRegistro;
    private EstadoCultivoStatus estado;
    private BigDecimal alturaCm;
    private BigDecimal humedadSustratoPct;
    private BigDecimal temperaturaC;
    private String observaciones;
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
     * Gets the status timestamp.
     *
     * @return status timestamp
     */
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Sets the status timestamp.
     *
     * @param fechaRegistro status timestamp
     */
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * Gets the crop status.
     *
     * @return crop status
     */
    public EstadoCultivoStatus getEstado() {
        return estado;
    }

    /**
     * Sets the crop status.
     *
     * @param estado crop status
     */
    public void setEstado(EstadoCultivoStatus estado) {
        this.estado = estado;
    }

    /**
     * Gets the height in cm.
     *
     * @return height in cm
     */
    public BigDecimal getAlturaCm() {
        return alturaCm;
    }

    /**
     * Sets the height in cm.
     *
     * @param alturaCm height in cm
     */
    public void setAlturaCm(BigDecimal alturaCm) {
        this.alturaCm = alturaCm;
    }

    /**
     * Gets the substrate humidity percent.
     *
     * @return humidity percent
     */
    public BigDecimal getHumedadSustratoPct() {
        return humedadSustratoPct;
    }

    /**
     * Sets the substrate humidity percent.
     *
     * @param humedadSustratoPct humidity percent
     */
    public void setHumedadSustratoPct(BigDecimal humedadSustratoPct) {
        this.humedadSustratoPct = humedadSustratoPct;
    }

    /**
     * Gets the temperature in celsius.
     *
     * @return temperature in celsius
     */
    public BigDecimal getTemperaturaC() {
        return temperaturaC;
    }

    /**
     * Sets the temperature in celsius.
     *
     * @param temperaturaC temperature in celsius
     */
    public void setTemperaturaC(BigDecimal temperaturaC) {
        this.temperaturaC = temperaturaC;
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
