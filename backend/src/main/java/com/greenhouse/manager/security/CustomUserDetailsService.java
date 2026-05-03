/*
 * Proyecto: GreenHouse Manager
 * Archivo: CustomUserDetailsService.java
 * Descripcion: Servicio de UserDetails para seguridad.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.security;

import com.greenhouse.manager.domain.entity.Usuario;
import com.greenhouse.manager.domain.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService implementation for JWT authentication.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Creates a new CustomUserDetailsService.
     *
     * @param usuarioRepository user repository
     */
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Loads a user by username (email).
     *
     * @param username email address
     * @return user details
     * @throws UsernameNotFoundException if user is missing
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("error.usuario.no_encontrado"));
        return CustomUserDetails.fromUsuario(usuario);
    }
}
