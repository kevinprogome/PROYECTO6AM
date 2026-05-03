/*
 * Proyecto: GreenHouse Manager
 * Archivo: InvernaderoRequest.java
 * Descripcion: DTO de solicitud para invernaderos.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Request DTO for Invernadero entity.
 */
public class InvernaderoRequest {

    @NotNull
    private Long usuarioId;

    @NotBlank
    @Size(max = 120)
    private String nombre;

    @NotBlank
    @Size(max = 180)
    private String ubicacion;

    @Size(max = 300)
    private String descripcion;

    private BigDecimal areaM2;

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
}
