/*
 * Proyecto: GreenHouse Manager
 * Archivo: AlertaRepository.java
 * Descripcion: Repositorio JPA para alertas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.domain.repository;

import com.greenhouse.manager.domain.entity.Alerta;
import com.greenhouse.manager.domain.enums.AlertaTipo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for Alerta entities.
 */
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    /**
     * Finds active alerts for a specific greenhouse.
     *
     * @param invernaderoId greenhouse id
     * @return list of active alerts
     */
    @Query("SELECT a FROM Alerta a WHERE a.invernadero.id = :invernaderoId AND a.activa = true")
    List<Alerta> findAlertasActivasPorInvernadero(@Param("invernaderoId") Long invernaderoId);

    /**
     * Checks whether an alert already exists for a plant/type in a date range.
     *
     * @param plantaId plant id
     * @param tipo alert type
     * @param inicio start date time
     * @param fin end date time
     * @return true if at least one alert exists
     */
    boolean existsByPlantaIdAndTipoAndFechaGeneracionBetween(
        Long plantaId,
        AlertaTipo tipo,
        LocalDateTime inicio,
        LocalDateTime fin
    );
}
