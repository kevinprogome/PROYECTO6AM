/*
 * Proyecto: GreenHouse Manager
 * Archivo: InvernaderoRepository.java
 * Descripcion: Repositorio JPA para invernaderos.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.domain.repository;

import com.greenhouse.manager.domain.entity.Invernadero;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Invernadero entities.
 */
public interface InvernaderoRepository extends JpaRepository<Invernadero, Long> {
    // Default CRUD methods from JpaRepository.
}
