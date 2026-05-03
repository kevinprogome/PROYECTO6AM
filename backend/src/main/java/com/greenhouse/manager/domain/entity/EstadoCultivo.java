/*
 * Proyecto: GreenHouse Manager
 * Archivo: EstadoCultivo.java
 * Descripcion: Entidad para estados del cultivo.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.domain.entity;

import com.greenhouse.manager.domain.enums.EstadoCultivoStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing crop status records.
 */
@Entity
@Table(name = "estados_cultivo")
public class EstadoCultivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "planta_id", nullable = false)
    private Planta planta;

    @NotNull
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EstadoCultivoStatus estado;

    @Column(name = "altura_cm", precision = 6, scale = 2)
    private BigDecimal alturaCm;

    @Column(name = "humedad_sustrato_pct", precision = 5, scale = 2)
    private BigDecimal humedadSustratoPct;

    @Column(name = "temperatura_c", precision = 5, scale = 2)
    private BigDecimal temperaturaC;

    @Size(max = 500)
    @Column(length = 500)
    private String observaciones;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor required by JPA.
     */
    public EstadoCultivo() {
        // Default constructor
    }

    /**
     * Initializes audit fields before persistence.
     */
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    /**
     * Gets the unique identifier.
     *
     * @return record id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier.
     *
     * @param id record id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the plant.
     *
     * @return plant
     */
    public Planta getPlanta() {
        return planta;
    }

    /**
     * Sets the plant.
     *
     * @param planta plant
     */
    public void setPlanta(Planta planta) {
        this.planta = planta;
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
