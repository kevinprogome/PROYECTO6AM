/*
 * Proyecto: GreenHouse Manager
 * Archivo: RegistroFertilizacionController.java
 * Descripcion: Controlador REST para registros de fertilizacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.controller;

import com.greenhouse.manager.api.dto.request.RegistroFertilizacionRequest;
import com.greenhouse.manager.api.dto.response.RegistroFertilizacionResponse;
import com.greenhouse.manager.config.OpenApiSecurity;
import com.greenhouse.manager.service.RegistroFertilizacionService;
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
 * REST controller for managing fertilization records.
 */
@RestController
@RequestMapping("/api/registros-fertilizacion")
@Tag(name = "Registros de Fertilizacion", description = "Gestion de registros de fertilizacion")
@OpenApiSecurity
public class RegistroFertilizacionController {

    private final RegistroFertilizacionService registroFertilizacionService;

    /**
     * Creates a new RegistroFertilizacionController.
     *
     * @param registroFertilizacionService fertilization record service
     */
    public RegistroFertilizacionController(RegistroFertilizacionService registroFertilizacionService) {
        this.registroFertilizacionService = registroFertilizacionService;
    }

    /**
     * Lists all fertilization records.
     *
     * @return list of records
     */
    @Operation(summary = "List fertilization records")
    @ApiResponse(responseCode = "200", description = "Ok")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping
    public List<RegistroFertilizacionResponse> getAllRegistrosFertilizacion() {
        return registroFertilizacionService.getAllRegistrosFertilizacion();
    }

    /**
     * Retrieves a fertilization record by id.
     *
     * @param id record id
     * @return record
     */
    @Operation(summary = "Get fertilization record by id")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping("/{id}")
    public RegistroFertilizacionResponse getRegistroFertilizacionById(@PathVariable Long id) {
        return registroFertilizacionService.getRegistroFertilizacionById(id);
    }

    /**
     * Creates a new fertilization record.
     *
     * @param request record request
     * @return created record
     */
    @Operation(summary = "Create fertilization record")
    @ApiResponse(responseCode = "201", description = "Created")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RegistroFertilizacionResponse createRegistroFertilizacion(
        @Valid @RequestBody RegistroFertilizacionRequest request
    ) {
        return registroFertilizacionService.createRegistroFertilizacion(request);
    }

    /**
     * Updates a fertilization record.
     *
     * @param id record id
     * @param request record request
     * @return updated record
     */
    @Operation(summary = "Update fertilization record")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @PutMapping("/{id}")
    public RegistroFertilizacionResponse updateRegistroFertilizacion(
        @PathVariable Long id,
        @Valid @RequestBody RegistroFertilizacionRequest request
    ) {
        return registroFertilizacionService.updateRegistroFertilizacion(id, request);
    }

    /**
     * Deletes a fertilization record.
     *
     * @param id record id
     */
    @Operation(summary = "Delete fertilization record")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteRegistroFertilizacion(@PathVariable Long id) {
        registroFertilizacionService.deleteRegistroFertilizacion(id);
    }
}
