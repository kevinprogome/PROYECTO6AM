/*
 * Proyecto: GreenHouse Manager
 * Archivo: EstadoCultivoController.java
 * Descripcion: Controlador REST para estados de cultivo.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.controller;

import com.greenhouse.manager.api.dto.request.EstadoCultivoRequest;
import com.greenhouse.manager.api.dto.response.EstadoCultivoResponse;
import com.greenhouse.manager.config.OpenApiSecurity;
import com.greenhouse.manager.service.EstadoCultivoService;
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
 * REST controller for managing crop status records.
 */
@RestController
@RequestMapping("/api/estados-cultivo")
@Tag(name = "Estados de Cultivo", description = "Gestion de estados de cultivo")
@OpenApiSecurity
public class EstadoCultivoController {

    private final EstadoCultivoService estadoCultivoService;

    /**
     * Creates a new EstadoCultivoController.
     *
     * @param estadoCultivoService crop status service
     */
    public EstadoCultivoController(EstadoCultivoService estadoCultivoService) {
        this.estadoCultivoService = estadoCultivoService;
    }

    /**
     * Lists all crop status records.
     *
     * @return list of records
     */
    @Operation(summary = "List crop status records")
    @ApiResponse(responseCode = "200", description = "Ok")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping
    public List<EstadoCultivoResponse> getAllEstadosCultivo() {
        return estadoCultivoService.getAllEstadosCultivo();
    }

    /**
     * Retrieves a crop status record by id.
     *
     * @param id record id
     * @return record
     */
    @Operation(summary = "Get crop status record by id")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping("/{id}")
    public EstadoCultivoResponse getEstadoCultivoById(@PathVariable Long id) {
        return estadoCultivoService.getEstadoCultivoById(id);
    }

    /**
     * Creates a new crop status record.
     *
     * @param request record request
     * @return created record
     */
    @Operation(summary = "Create crop status record")
    @ApiResponse(responseCode = "201", description = "Created")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EstadoCultivoResponse createEstadoCultivo(@Valid @RequestBody EstadoCultivoRequest request) {
        return estadoCultivoService.createEstadoCultivo(request);
    }

    /**
     * Updates a crop status record.
     *
     * @param id record id
     * @param request record request
     * @return updated record
     */
    @Operation(summary = "Update crop status record")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @PutMapping("/{id}")
    public EstadoCultivoResponse updateEstadoCultivo(
        @PathVariable Long id,
        @Valid @RequestBody EstadoCultivoRequest request
    ) {
        return estadoCultivoService.updateEstadoCultivo(id, request);
    }

    /**
     * Deletes a crop status record.
     *
     * @param id record id
     */
    @Operation(summary = "Delete crop status record")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEstadoCultivo(@PathVariable Long id) {
        estadoCultivoService.deleteEstadoCultivo(id);
    }
}
