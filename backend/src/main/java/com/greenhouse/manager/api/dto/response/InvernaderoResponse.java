/*
 * Proyecto: GreenHouse Manager
 * Archivo: InvernaderoResponse.java
 * Descripcion: DTO de respuesta para invernaderos.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for Invernadero entity.
 */
public class InvernaderoResponse {

    private Long id;
    private Long usuarioId;
    private String nombre;
    private String ubicacion;
    private String descripcion;
    private BigDecimal areaM2;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Gets the greenhouse id.
     *
     * @return greenhouse id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the greenhouse id.
     *
     * @param id greenhouse id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the owner user id.
     *
     * @return user id
     */
    public Long getUsuarioId() {
        return usuarioId;
    }

    /**
     * Sets the owner user id.
     *
     * @param usuarioId user id
     */
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
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
