/*
 * Proyecto: GreenHouse Manager
 * Archivo: Invernadero.java
 * Descripcion: Entidad para invernaderos.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a greenhouse.
 */
@Entity
@Table(name = "invernaderos")
public class Invernadero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotBlank
    @Size(max = 120)
    @Column(length = 120, nullable = false)
    private String nombre;

    @NotBlank
    @Size(max = 180)
    @Column(length = 180, nullable = false)
    private String ubicacion;

    @Size(max = 300)
    @Column(length = 300)
    private String descripcion;

    @Column(name = "area_m2", precision = 10, scale = 2)
    private BigDecimal areaM2;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "invernadero", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Planta> plantas = new ArrayList<>();

    @OneToMany(mappedBy = "invernadero", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alerta> alertas = new ArrayList<>();

    /**
     * Default constructor required by JPA.
     */
    public Invernadero() {
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
     * @return greenhouse id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier.
     *
     * @param id greenhouse id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the owner user.
     *
     * @return user owner
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Sets the owner user.
     *
     * @param usuario user owner
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Gets the greenhouse name.
     *
     * @return greenhouse name
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the greenhouse name.
     *
     * @param nombre greenhouse name
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Gets the location.
     *
     * @return location
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Sets the location.
     *
     * @param ubicacion location
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Gets the description.
     *
     * @return description
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the description.
     *
     * @param descripcion description
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Gets the area in square meters.
     *
     * @return area in m2
     */
    public BigDecimal getAreaM2() {
        return areaM2;
    }

    /**
     * Sets the area in square meters.
     *
     * @param areaM2 area in m2
     */
    public void setAreaM2(BigDecimal areaM2) {
        this.areaM2 = areaM2;
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
     * Gets the plants in the greenhouse.
     *
     * @return list of plants
     */
    public List<Planta> getPlantas() {
        return plantas;
    }

    /**
     * Sets the plants in the greenhouse.
     *
     * @param plantas list of plants
     */
    public void setPlantas(List<Planta> plantas) {
        this.plantas = plantas;
    }

    /**
     * Gets the alerts for the greenhouse.
     *
     * @return list of alerts
     */
    public List<Alerta> getAlertas() {
        return alertas;
    }

    /**
     * Sets the alerts for the greenhouse.
     *
     * @param alertas list of alerts
     */
    public void setAlertas(List<Alerta> alertas) {
        this.alertas = alertas;
    }
}
