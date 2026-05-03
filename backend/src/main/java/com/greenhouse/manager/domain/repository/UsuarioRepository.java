/*
 * Proyecto: GreenHouse Manager
 * Archivo: UsuarioRepository.java
 * Descripcion: Repositorio JPA para usuarios.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.domain.repository;

import com.greenhouse.manager.domain.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Usuario entities.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Finds a user by email.
     *
     * @param email user email
     * @return optional user
     */
    Optional<Usuario> findByEmail(String email);
}
