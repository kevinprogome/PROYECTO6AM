/*
 * Proyecto: GreenHouse Manager
 * Archivo: UsuarioResponse.java
 * Descripcion: DTO de respuesta para usuarios.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.dto.response;

import com.greenhouse.manager.domain.enums.AuthProvider;
import com.greenhouse.manager.domain.enums.UserRole;
import java.time.LocalDateTime;

/**
 * Response DTO for Usuario entity.
 */
public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String email;
    private UserRole rol;
    private AuthProvider provider;
    private String providerId;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Gets the user id.
     *
     * @return user id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the user id.
     *
     * @param id user id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the full name.
     *
     * @return full name
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the full name.
     *
     * @param nombre full name
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Gets the email address.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user role.
     *
     * @return user role
     */
    public UserRole getRol() {
        return rol;
    }

    /**
     * Sets the user role.
     *
     * @param rol user role
     */
    public void setRol(UserRole rol) {
        this.rol = rol;
    }

    /**
     * Gets the auth provider.
     *
     * @return auth provider
     */
    public AuthProvider getProvider() {
        return provider;
    }

    /**
     * Sets the auth provider.
     *
     * @param provider auth provider
     */
    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    /**
     * Gets the provider user id.
     *
     * @return provider user id
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * Sets the provider user id.
     *
     * @param providerId provider user id
     */
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    /**
     * Gets whether the user is active.
     *
     * @return active flag
     */
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Sets whether the user is active.
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
