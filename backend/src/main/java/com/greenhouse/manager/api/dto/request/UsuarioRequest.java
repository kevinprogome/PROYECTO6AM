/*
 * Proyecto: GreenHouse Manager
 * Archivo: UsuarioRequest.java
 * Descripcion: DTO de solicitud para usuarios.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.dto.request;

import com.greenhouse.manager.domain.enums.AuthProvider;
import com.greenhouse.manager.domain.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for Usuario entity.
 */
public class UsuarioRequest {

    @NotBlank
    @Size(max = 120)
    private String nombre;

    @NotBlank
    @Size(max = 180)
    private String email;

    @NotNull
    private UserRole rol;

    @NotNull
    private AuthProvider provider;

    @NotBlank
    @Size(max = 120)
    private String providerId;

    @NotNull
    private Boolean activo;

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
}
