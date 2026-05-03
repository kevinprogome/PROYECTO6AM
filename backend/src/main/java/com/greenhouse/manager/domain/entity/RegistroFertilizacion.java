/*
 * Proyecto: GreenHouse Manager
 * Archivo: RegistroFertilizacion.java
 * Descripcion: Entidad para registros de fertilizacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing fertilization records.
 */
@Entity
@Table(name = "registros_fertilizacion")
public class RegistroFertilizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "planta_id", nullable = false)
    private Planta planta;

    @NotNull
    @Column(name = "fecha_fertilizacion", nullable = false)
    private LocalDateTime fechaFertilizacion;

    @NotBlank
    @Size(max = 120)
    @Column(name = "tipo_fertilizante", length = 120, nullable = false)
    private String tipoFertilizante;

    @Column(precision = 8, scale = 2)
    private BigDecimal dosis;

    @Size(max = 20)
    @Column(length = 20)
    private String unidad;

    @Size(max = 120)
    @Column(length = 120)
    private String responsable;

    @Size(max = 300)
    @Column(length = 300)
    private String notas;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor required by JPA.
     */
    public RegistroFertilizacion() {
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
