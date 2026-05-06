/*
 * Proyecto: GreenHouse Manager
 * Archivo: AdminController.java
 * Descripcion: Endpoints administrativos para automatizacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-06
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.controller;

import com.greenhouse.manager.scheduler.AlertaScheduler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Administrative endpoints for automation.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Acciones administrativas")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AlertaScheduler alertaScheduler;

    /**
     * Creates a new AdminController.
     *
     * @param alertaScheduler scheduler service
     */
    public AdminController(AlertaScheduler alertaScheduler) {
        this.alertaScheduler = alertaScheduler;
    }

    /**
     * Triggers the alert scheduler manually.
     *
     * @return scheduler execution summary
     */
    @Operation(summary = "Trigger alert scheduler")
    @PostMapping("/trigger-scheduler")
    public SchedulerTriggerResponse triggerScheduler() {
        Map<Long, Integer> resultados = alertaScheduler.generarAlertasAutomaticas(LocalDate.now());
        int total = resultados.values().stream().mapToInt(Integer::intValue).sum();
        Map<Long, Integer> porInvernadero = new HashMap<>(resultados);
        return new SchedulerTriggerResponse(total, porInvernadero);
    }

    /**
     * Response payload for scheduler trigger results.
     */
    public static class SchedulerTriggerResponse {

        private final int total;
        private final Map<Long, Integer> byGreenhouse;

        /**
         * Creates a new scheduler response.
         *
         * @param total total alerts generated
         * @param byGreenhouse counts by greenhouse
         */
        public SchedulerTriggerResponse(int total, Map<Long, Integer> byGreenhouse) {
            this.total = total;
            this.byGreenhouse = byGreenhouse;
        }

        /**
         * Gets total alerts generated.
         *
         * @return total alerts
         */
        public int getTotal() {
            return total;
        }

        /**
         * Gets alert counts by greenhouse.
         *
         * @return map of greenhouse id to count
         */
        public Map<Long, Integer> getByGreenhouse() {
            return byGreenhouse;
        }
    }
}
