/*
 * Proyecto: GreenHouse Manager
 * Archivo: PlantaService.java
 * Descripcion: Servicio de negocio para plantas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.service;

import com.greenhouse.manager.api.dto.request.PlantaRequest;
import com.greenhouse.manager.api.dto.response.PlantaResponse;
import com.greenhouse.manager.api.exception.NotFoundException;
import com.greenhouse.manager.domain.entity.Invernadero;
import com.greenhouse.manager.domain.entity.Planta;
import com.greenhouse.manager.domain.enums.EstadoCultivoStatus;
import com.greenhouse.manager.domain.repository.InvernaderoRepository;
import com.greenhouse.manager.domain.repository.PlantaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing plants.
 */
@Service
public class PlantaService {

    private final PlantaRepository plantaRepository;
    private final InvernaderoRepository invernaderoRepository;

    /**
     * Creates a new PlantaService instance.
     *
     * @param plantaRepository repository for plants
     * @param invernaderoRepository repository for greenhouses
     */
    public PlantaService(
        PlantaRepository plantaRepository,
        InvernaderoRepository invernaderoRepository
    ) {
        this.plantaRepository = plantaRepository;
        this.invernaderoRepository = invernaderoRepository;
    }

    /**
     * Creates a new plant.
     *
     * @param request plant request
     * @return created plant
     */
    @Transactional
    public PlantaResponse createPlanta(PlantaRequest request) {
        Planta planta = new Planta();
        applyRequest(planta, request);
        Planta saved = plantaRepository.save(planta);
        return toResponse(saved);
    }

    /**
     * Updates a plant.
     *
     * @param id plant id
     * @param request plant request
     * @return updated plant
     */
    @Transactional
    public PlantaResponse updatePlanta(Long id, PlantaRequest request) {
        Planta planta = getPlantaEntity(id);
        applyRequest(planta, request);
        Planta saved = plantaRepository.save(planta);
        return toResponse(saved);
    }

    /**
     * Retrieves a plant by id.
     *
     * @param id plant id
     * @return plant response
     */
    @Transactional(readOnly = true)
    public PlantaResponse getPlantaById(Long id) {
        return toResponse(getPlantaEntity(id));
    }

    /**
     * Retrieves all plants.
     *
     * @return list of plants
     */
    @Transactional(readOnly = true)
    public List<PlantaResponse> getAllPlantas() {
        return plantaRepository.findAll()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Deletes a plant by id.
     *
     * @param id plant id
     */
    @Transactional
    public void deletePlanta(Long id) {
        Planta planta = getPlantaEntity(id);
        plantaRepository.delete(planta);
    }

    /**
     * Retrieves plants by crop status.
     *
     * @param estado crop status
     * @return list of plants
     */
    @Transactional(readOnly = true)
    public List<PlantaResponse> getPlantasByEstado(EstadoCultivoStatus estado) {
        return plantaRepository.findByEstadoActual(estado)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves plants that need irrigation for a given date.
     *
     * @param fechaActual current date
     * @return list of plants
     */
    @Transactional(readOnly = true)
    public List<PlantaResponse> getPlantasQueNecesitanRiego(LocalDate fechaActual) {
        LocalDate fechaConsulta = fechaActual == null ? LocalDate.now() : fechaActual;
        return plantaRepository.findPlantasQueNecesitanRiego(fechaConsulta)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves plants that need fertilization for a given date.
     *
     * @param fechaActual current date
     * @return list of plants
     */
    @Transactional(readOnly = true)
    public List<PlantaResponse> getPlantasQueNecesitanFertilizacion(LocalDate fechaActual) {
        LocalDate fechaConsulta = fechaActual == null ? LocalDate.now() : fechaActual;
        return plantaRepository.findAll()
            .stream()
            .filter(planta -> Boolean.TRUE.equals(planta.getActivo()))
            .filter(planta -> necesitaFertilizacion(planta, fechaConsulta))
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Determines whether a plant needs irrigation.
     *
     * @param planta plant entity
     * @return true if needs irrigation
     */
    @Transactional(readOnly = true)
    public boolean necesitaRiego(Planta planta) {
        return necesitaRiego(planta, LocalDate.now());
    }

    /**
     * Determines whether a plant needs irrigation by id.
     *
     * @param plantaId plant id
     * @return true if needs irrigation
     */
    @Transactional(readOnly = true)
    public boolean necesitaRiego(Long plantaId) {
        return necesitaRiego(getPlantaEntity(plantaId), LocalDate.now());
    }

    /**
     * Determines whether a plant needs fertilization.
     *
     * @param planta plant entity
     * @return true if needs fertilization
     */
    @Transactional(readOnly = true)
    public boolean necesitaFertilizacion(Planta planta) {
        return necesitaFertilizacion(planta, LocalDate.now());
    }

    /**
     * Determines whether a plant needs fertilization by id.
     *
     * @param plantaId plant id
     * @return true if needs fertilization
     */
    @Transactional(readOnly = true)
    public boolean necesitaFertilizacion(Long plantaId) {
        return necesitaFertilizacion(getPlantaEntity(plantaId), LocalDate.now());
    }

    private Planta getPlantaEntity(Long id) {
        return plantaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.planta.no_encontrada"));
    }

    private Invernadero getInvernaderoEntity(Long id) {
        return invernaderoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.invernadero.no_encontrado"));
    }

    private void applyRequest(Planta planta, PlantaRequest request) {
        planta.setInvernadero(getInvernaderoEntity(request.getInvernaderoId()));
        planta.setNombreComun(request.getNombreComun());
        planta.setNombreCientifico(request.getNombreCientifico());
        planta.setVariedad(request.getVariedad());
        planta.setFechaSiembra(request.getFechaSiembra());
        planta.setFechaUltimoRiego(request.getFechaUltimoRiego());
        planta.setFrecuenciaRiegoDias(request.getFrecuenciaRiegoDias());
        planta.setFechaUltimaFertilizacion(request.getFechaUltimaFertilizacion());
        planta.setFrecuenciaFertilizacionDias(request.getFrecuenciaFertilizacionDias());
        planta.setEstadoActual(request.getEstadoActual());
        planta.setObservaciones(request.getObservaciones());
        planta.setActivo(request.getActivo());
    }

    private PlantaResponse toResponse(Planta planta) {
        PlantaResponse response = new PlantaResponse();
        response.setId(planta.getId());
        response.setInvernaderoId(planta.getInvernadero().getId());
        response.setNombreComun(planta.getNombreComun());
        response.setNombreCientifico(planta.getNombreCientifico());
        response.setVariedad(planta.getVariedad());
        response.setFechaSiembra(planta.getFechaSiembra());
        response.setFechaUltimoRiego(planta.getFechaUltimoRiego());
        response.setFrecuenciaRiegoDias(planta.getFrecuenciaRiegoDias());
        response.setFechaUltimaFertilizacion(planta.getFechaUltimaFertilizacion());
        response.setFrecuenciaFertilizacionDias(planta.getFrecuenciaFertilizacionDias());
        response.setEstadoActual(planta.getEstadoActual());
        response.setObservaciones(planta.getObservaciones());
        response.setActivo(planta.getActivo());
        response.setCreatedAt(planta.getCreatedAt());
        response.setUpdatedAt(planta.getUpdatedAt());
        return response;
    }

    private boolean necesitaRiego(Planta planta, LocalDate fechaActual) {
        if (planta.getFechaUltimoRiego() == null) {
            return true;
        }
        LocalDate proximoRiego = planta.getFechaUltimoRiego()
            .plusDays(planta.getFrecuenciaRiegoDias());
        return !proximoRiego.isAfter(fechaActual);
    }

    private boolean necesitaFertilizacion(Planta planta, LocalDate fechaActual) {
        if (planta.getFechaUltimaFertilizacion() == null) {
            return true;
        }
        LocalDate proximaFertilizacion = planta.getFechaUltimaFertilizacion()
            .plusDays(planta.getFrecuenciaFertilizacionDias());
        return !proximaFertilizacion.isAfter(fechaActual);
    }
}
