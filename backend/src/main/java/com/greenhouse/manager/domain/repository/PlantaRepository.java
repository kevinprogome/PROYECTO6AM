/*
 * Proyecto: GreenHouse Manager
 * Archivo: PlantaRepository.java
 * Descripcion: Repositorio JPA para plantas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.domain.repository;

import com.greenhouse.manager.domain.entity.Planta;
import com.greenhouse.manager.domain.enums.EstadoCultivoStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for Planta entities.
 */
public interface PlantaRepository extends JpaRepository<Planta, Long> {

    /**
     * Finds plants by crop status.
     *
     * @param estado status to filter
     * @return list of plants
     */
    @Query("SELECT p FROM Planta p WHERE p.estadoActual = :estado")
    List<Planta> findByEstadoActual(@Param("estado") EstadoCultivoStatus estado);

    /**
     * Finds plants that need irrigation based on last irrigation date and frequency.
     *
     * @param fechaActual current date
     * @return list of plants due for irrigation
     */
    @Query(value = "SELECT * FROM plantas p "
        + "WHERE p.activo = 1 "
        + "AND (p.fecha_ultimo_riego IS NULL "
        + "OR DATE_ADD(p.fecha_ultimo_riego, INTERVAL p.frecuencia_riego_dias DAY) <= :fechaActual)",
        nativeQuery = true)
    List<Planta> findPlantasQueNecesitanRiego(@Param("fechaActual") LocalDate fechaActual);
}
