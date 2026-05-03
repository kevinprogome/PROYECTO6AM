/*
 * Proyecto: GreenHouse Manager
 * Archivo: RegistroRiegoController.java
 * Descripcion: Controlador REST para registros de riego.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.controller;

import com.greenhouse.manager.api.dto.request.RegistroRiegoRequest;
import com.greenhouse.manager.api.dto.response.RegistroRiegoResponse;
import com.greenhouse.manager.config.OpenApiSecurity;
import com.greenhouse.manager.service.RegistroRiegoService;
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
 * REST controller for managing irrigation records.
 */
@RestController
@RequestMapping("/api/registros-riego")
@Tag(name = "Registros de Riego", description = "Gestion de registros de riego")
@OpenApiSecurity
public class RegistroRiegoController {

    private final RegistroRiegoService registroRiegoService;

    /**
     * Creates a new RegistroRiegoController.
     *
     * @param registroRiegoService irrigation record service
     */
    public RegistroRiegoController(RegistroRiegoService registroRiegoService) {
        this.registroRiegoService = registroRiegoService;
    }

    /**
     * Lists all irrigation records.
     *
     * @return list of records
     */
    @Operation(summary = "List irrigation records")
    @ApiResponse(responseCode = "200", description = "Ok")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping
    public List<RegistroRiegoResponse> getAllRegistrosRiego() {
        return registroRiegoService.getAllRegistrosRiego();
    }

    /**
     * Retrieves an irrigation record by id.
     *
     * @param id record id
     * @return record
     */
    @Operation(summary = "Get irrigation record by id")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping("/{id}")
    public RegistroRiegoResponse getRegistroRiegoById(@PathVariable Long id) {
        return registroRiegoService.getRegistroRiegoById(id);
    }

    /**
     * Creates a new irrigation record.
     *
     * @param request record request
     * @return created record
     */
    @Operation(summary = "Create irrigation record")
    @ApiResponse(responseCode = "201", description = "Created")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RegistroRiegoResponse createRegistroRiego(@Valid @RequestBody RegistroRiegoRequest request) {
        return registroRiegoService.createRegistroRiego(request);
    }

    /**
     * Updates an irrigation record.
     *
     * @param id record id
     * @param request record request
     * @return updated record
     */
    @Operation(summary = "Update irrigation record")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @PutMapping("/{id}")
    public RegistroRiegoResponse updateRegistroRiego(
        @PathVariable Long id,
        @Valid @RequestBody RegistroRiegoRequest request
    ) {
        return registroRiegoService.updateRegistroRiego(id, request);
    }

    /**
     * Deletes an irrigation record.
     *
     * @param id record id
     */
    @Operation(summary = "Delete irrigation record")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteRegistroRiego(@PathVariable Long id) {
        registroRiegoService.deleteRegistroRiego(id);
    }
}
