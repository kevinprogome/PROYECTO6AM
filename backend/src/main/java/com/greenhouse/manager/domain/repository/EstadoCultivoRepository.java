/*
 * Proyecto: GreenHouse Manager
 * Archivo: EstadoCultivoRepository.java
 * Descripcion: Repositorio JPA para estados del cultivo.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.domain.repository;

import com.greenhouse.manager.domain.entity.EstadoCultivo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for EstadoCultivo entities.
 */
public interface EstadoCultivoRepository extends JpaRepository<EstadoCultivo, Long> {
    // Default CRUD methods from JpaRepository.
}
