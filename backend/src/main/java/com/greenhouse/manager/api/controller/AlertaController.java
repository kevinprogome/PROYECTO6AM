/*
 * Proyecto: GreenHouse Manager
 * Archivo: AlertaController.java
 * Descripcion: Controlador REST para alertas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.controller;

import com.greenhouse.manager.api.dto.request.AlertaRequest;
import com.greenhouse.manager.api.dto.response.AlertaResponse;
import com.greenhouse.manager.config.OpenApiSecurity;
import com.greenhouse.manager.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing alerts.
 */
@RestController
@RequestMapping("/api/alertas")
@Tag(name = "Alertas", description = "Gestion de alertas")
@OpenApiSecurity
public class AlertaController {

    private final AlertaService alertaService;

    /**
     * Creates a new AlertaController.
     *
     * @param alertaService alert service
     */
    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    /**
     * Lists all alerts.
     *
     * @return list of alerts
     */
    @Operation(summary = "List alerts")
    @ApiResponse(responseCode = "200", description = "Ok")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping
    public List<AlertaResponse> getAllAlertas() {
        return alertaService.getAllAlertas();
    }

    /**
     * Retrieves an alert by id.
     *
     * @param id alert id
     * @return alert
     */
    @Operation(summary = "Get alert by id")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping("/{id}")
    public AlertaResponse getAlertaById(@PathVariable Long id) {
        return alertaService.getAlertaById(id);
    }

    /**
     * Creates a new alert.
     *
     * @param request alert request
     * @return created alert
     */
    @Operation(summary = "Create alert")
    @ApiResponse(responseCode = "201", description = "Created")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AlertaResponse createAlerta(@Valid @RequestBody AlertaRequest request) {
        return alertaService.createAlerta(request);
    }

    /**
     * Updates an alert.
     *
     * @param id alert id
     * @param request alert request
     * @return updated alert
     */
    @Operation(summary = "Update alert")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @PutMapping("/{id}")
    public AlertaResponse updateAlerta(@PathVariable Long id, @Valid @RequestBody AlertaRequest request) {
        return alertaService.updateAlerta(id, request);
    }

    /**
     * Deletes an alert.
     *
     * @param id alert id
     */
    @Operation(summary = "Delete alert")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteAlerta(@PathVariable Long id) {
        alertaService.deleteAlerta(id);
    }

    /**
     * Lists active alerts for a greenhouse.
     *
     * @param invernaderoId greenhouse id
     * @return list of active alerts
     */
    @Operation(summary = "List active alerts by greenhouse")
    @ApiResponse(responseCode = "200", description = "Ok")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping("/activos/invernadero/{invernaderoId}")
    public List<AlertaResponse> getAlertasActivasPorInvernadero(@PathVariable Long invernaderoId) {
        return alertaService.getAlertasActivasPorInvernadero(invernaderoId);
    }
}
