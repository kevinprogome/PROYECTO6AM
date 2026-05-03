/*
 * Proyecto: GreenHouse Manager
 * Archivo: RegistroFertilizacionRepository.java
 * Descripcion: Repositorio JPA para registros de fertilizacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.domain.repository;

import com.greenhouse.manager.domain.entity.RegistroFertilizacion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for RegistroFertilizacion entities.
 */
public interface RegistroFertilizacionRepository extends JpaRepository<RegistroFertilizacion, Long> {
    // Default CRUD methods from JpaRepository.
}
