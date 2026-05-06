/*
 * Proyecto: GreenHouse Manager
 * Archivo: InvernaderoController.java
 * Descripcion: Controlador REST para invernaderos.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.controller;

import com.greenhouse.manager.api.dto.request.InvernaderoRequest;
import com.greenhouse.manager.api.dto.response.InvernaderoResponse;
import com.greenhouse.manager.api.dto.response.InvernaderoStatsResponse;
import com.greenhouse.manager.config.OpenApiSecurity;
import com.greenhouse.manager.service.InvernaderoService;
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
 * REST controller for managing greenhouses.
 */
@RestController
@RequestMapping("/api/invernaderos")
@Tag(name = "Invernaderos", description = "Gestion de invernaderos")
@OpenApiSecurity
public class InvernaderoController {

    private final InvernaderoService invernaderoService;

    /**
     * Creates a new InvernaderoController.
     *
     * @param invernaderoService greenhouse service
     */
    public InvernaderoController(InvernaderoService invernaderoService) {
        this.invernaderoService = invernaderoService;
    }

    /**
     * Lists all greenhouses.
     *
     * @return list of greenhouses
     */
    @Operation(summary = "List greenhouses")
    @ApiResponse(responseCode = "200", description = "Ok")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping
    public List<InvernaderoResponse> getAllInvernaderos() {
        return invernaderoService.getAllInvernaderos();
    }

    /**
     * Retrieves a greenhouse by id.
     *
     * @param id greenhouse id
     * @return greenhouse
     */
    @Operation(summary = "Get greenhouse by id")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping("/{id}")
    public InvernaderoResponse getInvernaderoById(@PathVariable Long id) {
        return invernaderoService.getInvernaderoById(id);
    }

    /**
     * Creates a new greenhouse.
     *
     * @param request greenhouse request
     * @return created greenhouse
     */
    @Operation(summary = "Create greenhouse")
    @ApiResponse(responseCode = "201", description = "Created")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public InvernaderoResponse createInvernadero(@Valid @RequestBody InvernaderoRequest request) {
        return invernaderoService.createInvernadero(request);
    }

    /**
     * Updates a greenhouse.
     *
     * @param id greenhouse id
     * @param request greenhouse request
     * @return updated greenhouse
     */
    @Operation(summary = "Update greenhouse")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @PutMapping("/{id}")
    public InvernaderoResponse updateInvernadero(
        @PathVariable Long id,
        @Valid @RequestBody InvernaderoRequest request
    ) {
        return invernaderoService.updateInvernadero(id, request);
    }

    /**
     * Deletes a greenhouse.
     *
     * @param id greenhouse id
     */
    @Operation(summary = "Delete greenhouse")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteInvernadero(@PathVariable Long id) {
        invernaderoService.deleteInvernadero(id);
    }

    /**
     * Retrieves greenhouse statistics.
     *
     * @param id greenhouse id
     * @return greenhouse stats
     */
    @Operation(summary = "Get greenhouse stats")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping("/{id}/stats")
    public InvernaderoStatsResponse getInvernaderoStats(@PathVariable Long id) {
        return invernaderoService.getInvernaderoStats(id);
    }
}
