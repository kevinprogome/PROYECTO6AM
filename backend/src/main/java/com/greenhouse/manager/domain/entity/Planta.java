/*
 * Proyecto: GreenHouse Manager
 * Archivo: Planta.java
 * Descripcion: Entidad para plantas y cultivos.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.domain.entity;

import com.greenhouse.manager.domain.enums.EstadoCultivoStatus;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a plant within a greenhouse.
 */
@Entity
@Table(name = "plantas")
public class Planta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invernadero_id", nullable = false)
    private Invernadero invernadero;

    @NotBlank
    @Size(max = 120)
    @Column(name = "nombre_comun", length = 120, nullable = false)
    private String nombreComun;

    @Size(max = 180)
    @Column(name = "nombre_cientifico", length = 180)
    private String nombreCientifico;

    @Size(max = 120)
    @Column(length = 120)
    private String variedad;

    @Column(name = "fecha_siembra")
    private LocalDate fechaSiembra;

    @Column(name = "fecha_ultimo_riego")
    private LocalDate fechaUltimoRiego;

    @NotNull
    @Column(name = "frecuencia_riego_dias", nullable = false)
    private Integer frecuenciaRiegoDias;

    @Column(name = "fecha_ultima_fertilizacion")
    private LocalDate fechaUltimaFertilizacion;

    @NotNull
    @Column(name = "frecuencia_fertilizacion_dias", nullable = false)
    private Integer frecuenciaFertilizacionDias;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_actual", length = 20, nullable = false)
    private EstadoCultivoStatus estadoActual;

    @Size(max = 500)
    @Column(length = 500)
    private String observaciones;

    @NotNull
    @Column(nullable = false)
    private Boolean activo;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "planta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroRiego> registrosRiego = new ArrayList<>();

    @OneToMany(mappedBy = "planta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroFertilizacion> registrosFertilizacion = new ArrayList<>();

    @OneToMany(mappedBy = "planta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstadoCultivo> estadosCultivo = new ArrayList<>();

    @OneToMany(mappedBy = "planta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alerta> alertas = new ArrayList<>();

    /**
     * Default constructor required by JPA.
     */
    public Planta() {
        // Default constructor
    }

    /**
     * Initializes audit fields before persistence.
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    /**
     * Updates audit fields before update operations.
     */
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Gets the unique identifier.
     *
     * @return plant id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier.
     *
     * @param id plant id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the greenhouse that owns the plant.
     *
     * @return greenhouse
     */
    public Invernadero getInvernadero() {
        return invernadero;
    }

    /**
     * Sets the greenhouse that owns the plant.
     *
     * @param invernadero greenhouse
     */
    public void setInvernadero(Invernadero invernadero) {
        this.invernadero = invernadero;
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
     * Gets the current crop status.
     *
     * @return crop status
     */
    public EstadoCultivoStatus getEstadoActual() {
        return estadoActual;
    }

    /**
     * Sets the current crop status.
     *
     * @param estadoActual crop status
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
     * Gets the last update timestamp.
     *
     * @return updated at timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp.
     *
     * @param updatedAt updated at timestamp
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the irrigation records.
     *
     * @return irrigation records
     */
    public List<RegistroRiego> getRegistrosRiego() {
        return registrosRiego;
    }

    /**
     * Sets the irrigation records.
     *
     * @param registrosRiego irrigation records
     */
    public void setRegistrosRiego(List<RegistroRiego> registrosRiego) {
        this.registrosRiego = registrosRiego;
    }

    /**
     * Gets the fertilization records.
     *
     * @return fertilization records
     */
    public List<RegistroFertilizacion> getRegistrosFertilizacion() {
        return registrosFertilizacion;
    }

    /**
     * Sets the fertilization records.
     *
     * @param registrosFertilizacion fertilization records
     */
    public void setRegistrosFertilizacion(List<RegistroFertilizacion> registrosFertilizacion) {
        this.registrosFertilizacion = registrosFertilizacion;
    }

    /**
     * Gets the crop status history.
     *
     * @return crop status history
     */
    public List<EstadoCultivo> getEstadosCultivo() {
        return estadosCultivo;
    }

    /**
     * Sets the crop status history.
     *
     * @param estadosCultivo crop status history
     */
    public void setEstadosCultivo(List<EstadoCultivo> estadosCultivo) {
        this.estadosCultivo = estadosCultivo;
    }

    /**
     * Gets the alerts for the plant.
     *
     * @return alerts
     */
    public List<Alerta> getAlertas() {
        return alertas;
    }

    /**
     * Sets the alerts for the plant.
     *
     * @param alertas alerts
     */
    public void setAlertas(List<Alerta> alertas) {
        this.alertas = alertas;
    }
}
