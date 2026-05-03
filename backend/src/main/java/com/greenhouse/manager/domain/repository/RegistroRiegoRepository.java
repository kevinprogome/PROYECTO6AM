/*
 * Proyecto: GreenHouse Manager
 * Archivo: RegistroRiegoRepository.java
 * Descripcion: Repositorio JPA para registros de riego.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.domain.repository;

import com.greenhouse.manager.domain.entity.RegistroRiego;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for RegistroRiego entities.
 */
public interface RegistroRiegoRepository extends JpaRepository<RegistroRiego, Long> {
    // Default CRUD methods from JpaRepository.
}
