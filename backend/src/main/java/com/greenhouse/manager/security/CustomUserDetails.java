/*
 * Proyecto: GreenHouse Manager
 * Archivo: CustomUserDetails.java
 * Descripcion: Implementacion de UserDetails basada en Usuario.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.security;

import com.greenhouse.manager.domain.entity.Usuario;
import com.greenhouse.manager.domain.enums.UserRole;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserDetails implementation for application users.
 */
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final UserRole role;
    private final boolean active;

    /**
     * Creates a new CustomUserDetails.
     *
     * @param id user id
     * @param email user email
     * @param role user role
     * @param active user active flag
     */
    public CustomUserDetails(Long id, String email, UserRole role, boolean active) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    /**
     * Builds a CustomUserDetails from a Usuario entity.
     *
     * @param usuario user entity
     * @return user details
     */
    public static CustomUserDetails fromUsuario(Usuario usuario) {
        boolean active = Boolean.TRUE.equals(usuario.getActivo());
        return new CustomUserDetails(usuario.getId(), usuario.getEmail(), usuario.getRol(), active);
    }

    /**
     * Gets the user id.
     *
     * @return user id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the user role.
     *
     * @return user role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Gets the granted authorities.
     *
     * @return authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * Gets the password placeholder.
     *
     * @return password
     */
    @Override
    public String getPassword() {
        return "";
    }

    /**
     * Gets the username (email).
     *
     * @return username
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates whether the account is non-expired.
     *
     * @return true if non-expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the account is non-locked.
     *
     * @return true if non-locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the credentials are non-expired.
     *
     * @return true if non-expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled.
     *
     * @return true if enabled
     */
    @Override
    public boolean isEnabled() {
        return active;
    }
}
