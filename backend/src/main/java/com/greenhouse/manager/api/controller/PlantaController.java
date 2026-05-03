/*
 * Proyecto: GreenHouse Manager
 * Archivo: PlantaController.java
 * Descripcion: Controlador REST para plantas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.controller;

import com.greenhouse.manager.api.dto.request.PlantaRequest;
import com.greenhouse.manager.api.dto.response.PlantaResponse;
import com.greenhouse.manager.config.OpenApiSecurity;
import com.greenhouse.manager.domain.enums.EstadoCultivoStatus;
import com.greenhouse.manager.service.PlantaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing plants.
 */
@RestController
@RequestMapping("/api/plantas")
@Tag(name = "Plantas", description = "Gestion de plantas")
@OpenApiSecurity
public class PlantaController {

    private final PlantaService plantaService;

    /**
     * Creates a new PlantaController.
     *
     * @param plantaService plant service
     */
    public PlantaController(PlantaService plantaService) {
        this.plantaService = plantaService;
    }

    /**
     * Lists all plants.
     *
     * @return list of plants
     */
    @Operation(summary = "List plants")
    @ApiResponse(responseCode = "200", description = "Ok")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping
    public List<PlantaResponse> getAllPlantas() {
        return plantaService.getAllPlantas();
    }

    /**
     * Retrieves a plant by id.
     *
     * @param id plant id
     * @return plant
     */
    @Operation(summary = "Get plant by id")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping("/{id}")
    public PlantaResponse getPlantaById(@PathVariable Long id) {
        return plantaService.getPlantaById(id);
    }

    /**
     * Creates a new plant.
     *
     * @param request plant request
     * @return created plant
     */
    @Operation(summary = "Create plant")
    @ApiResponse(responseCode = "201", description = "Created")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PlantaResponse createPlanta(@Valid @RequestBody PlantaRequest request) {
        return plantaService.createPlanta(request);
    }

    /**
     * Updates a plant.
     *
     * @param id plant id
     * @param request plant request
     * @return updated plant
     */
    @Operation(summary = "Update plant")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @PutMapping("/{id}")
    public PlantaResponse updatePlanta(@PathVariable Long id, @Valid @RequestBody PlantaRequest request) {
        return plantaService.updatePlanta(id, request);
    }

    /**
     * Deletes a plant.
     *
     * @param id plant id
     */
    @Operation(summary = "Delete plant")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletePlanta(@PathVariable Long id) {
        plantaService.deletePlanta(id);
    }

    /**
     * Lists plants by crop status.
     *
     * @param estado crop status
     * @return list of plants
     */
    @Operation(summary = "List plants by status")
    @ApiResponse(responseCode = "200", description = "Ok")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping("/estado/{estado}")
    public List<PlantaResponse> getPlantasByEstado(@PathVariable EstadoCultivoStatus estado) {
        return plantaService.getPlantasByEstado(estado);
    }

    /**
     * Lists plants that need irrigation.
     *
     * @param fecha optional date
     * @return list of plants
     */
    @Operation(summary = "List plants that need irrigation")
    @ApiResponse(responseCode = "200", description = "Ok")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping("/necesitan-riego")
    public List<PlantaResponse> getPlantasQueNecesitanRiego(
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        return plantaService.getPlantasQueNecesitanRiego(fecha);
    }

    /**
     * Lists plants that need fertilization.
     *
     * @param fecha optional date
     * @return list of plants
     */
    @Operation(summary = "List plants that need fertilization")
    @ApiResponse(responseCode = "200", description = "Ok")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @GetMapping("/necesitan-fertilizacion")
    public List<PlantaResponse> getPlantasQueNecesitanFertilizacion(
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        return plantaService.getPlantasQueNecesitanFertilizacion(fecha);
    }
}
