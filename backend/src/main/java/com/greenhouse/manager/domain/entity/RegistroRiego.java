/*
 * Proyecto: GreenHouse Manager
 * Archivo: RegistroRiego.java
 * Descripcion: Entidad para registros de riego.
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing irrigation records.
 */
@Entity
@Table(name = "registros_riego")
public class RegistroRiego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "planta_id", nullable = false)
    private Planta planta;

    @NotNull
    @Column(name = "fecha_riego", nullable = false)
    private LocalDateTime fechaRiego;

    @Column(name = "volumen_litros", precision = 8, scale = 2)
    private BigDecimal volumenLitros;

    @Size(max = 60)
    @Column(length = 60)
    private String metodo;

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
    public RegistroRiego() {
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
     * Gets the irrigation volume in liters.
     *
     * @return irrigation volume
     */
    public BigDecimal getVolumenLitros() {
        return volumenLitros;
    }

    /**
     * Sets the irrigation volume in liters.
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
