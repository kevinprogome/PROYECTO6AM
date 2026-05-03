/*
 * Proyecto: GreenHouse Manager
 * Archivo: Usuario.java
 * Descripcion: Entidad para usuarios del sistema.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.domain.entity;

import com.greenhouse.manager.domain.enums.AuthProvider;
import com.greenhouse.manager.domain.enums.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a system user.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    @Column(length = 120, nullable = false)
    private String nombre;

    @NotBlank
    @Size(max = 180)
    @Column(length = 180, nullable = false, unique = true)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserRole rol;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AuthProvider provider;

    @NotBlank
    @Size(max = 120)
    @Column(name = "provider_id", length = 120, nullable = false)
    private String providerId;

    @NotNull
    @Column(nullable = false)
    private Boolean activo;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invernadero> invernaderos = new ArrayList<>();

    /**
     * Default constructor required by JPA.
     */
    public Usuario() {
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
     * @return user id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier.
     *
     * @param id user id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the full name.
     *
     * @return user name
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the full name.
     *
     * @param nombre user name
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Gets the email address.
     *
     * @return user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email user email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the assigned role.
     *
     * @return user role
     */
    public UserRole getRol() {
        return rol;
    }

    /**
     * Sets the assigned role.
     *
     * @param rol user role
     */
    public void setRol(UserRole rol) {
        this.rol = rol;
    }

    /**
     * Gets the OAuth2 provider.
     *
     * @return auth provider
     */
    public AuthProvider getProvider() {
        return provider;
    }

    /**
     * Sets the OAuth2 provider.
     *
     * @param provider auth provider
     */
    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    /**
     * Gets the external provider user id.
     *
     * @return provider id
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * Sets the external provider user id.
     *
     * @param providerId provider id
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
     * Gets the greenhouses owned by the user.
     *
     * @return list of greenhouses
     */
    public List<Invernadero> getInvernaderos() {
        return invernaderos;
    }

    /**
     * Sets the greenhouses owned by the user.
     *
     * @param invernaderos list of greenhouses
     */
    public void setInvernaderos(List<Invernadero> invernaderos) {
        this.invernaderos = invernaderos;
    }
}
