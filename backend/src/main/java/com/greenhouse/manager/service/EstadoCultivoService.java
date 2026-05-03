/*
 * Proyecto: GreenHouse Manager
 * Archivo: EstadoCultivoService.java
 * Descripcion: Servicio de negocio para estados de cultivo.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.service;

import com.greenhouse.manager.api.dto.request.EstadoCultivoRequest;
import com.greenhouse.manager.api.dto.response.EstadoCultivoResponse;
import com.greenhouse.manager.api.exception.NotFoundException;
import com.greenhouse.manager.domain.entity.EstadoCultivo;
import com.greenhouse.manager.domain.entity.Planta;
import com.greenhouse.manager.domain.repository.EstadoCultivoRepository;
import com.greenhouse.manager.domain.repository.PlantaRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing crop status records.
 */
@Service
public class EstadoCultivoService {

    private final EstadoCultivoRepository estadoCultivoRepository;
    private final PlantaRepository plantaRepository;

    /**
     * Creates a new EstadoCultivoService instance.
     *
     * @param estadoCultivoRepository repository for crop status records
     * @param plantaRepository repository for plants
     */
    public EstadoCultivoService(
        EstadoCultivoRepository estadoCultivoRepository,
        PlantaRepository plantaRepository
    ) {
        this.estadoCultivoRepository = estadoCultivoRepository;
        this.plantaRepository = plantaRepository;
    }

    /**
     * Creates a crop status record.
     *
     * @param request crop status request
     * @return created record
     */
    @Transactional
    public EstadoCultivoResponse createEstadoCultivo(EstadoCultivoRequest request) {
        Planta planta = getPlantaEntity(request.getPlantaId());
        EstadoCultivo estado = new EstadoCultivo();
        estado.setPlanta(planta);
        estado.setFechaRegistro(request.getFechaRegistro());
        estado.setEstado(request.getEstado());
        estado.setAlturaCm(request.getAlturaCm());
        estado.setHumedadSustratoPct(request.getHumedadSustratoPct());
        estado.setTemperaturaC(request.getTemperaturaC());
        estado.setObservaciones(request.getObservaciones());

        EstadoCultivo saved = estadoCultivoRepository.save(estado);
        updateEstadoActual(planta, request.getEstado());
        return toResponse(saved);
    }

    /**
     * Updates a crop status record.
     *
     * @param id record id
     * @param request crop status request
     * @return updated record
     */
    @Transactional
    public EstadoCultivoResponse updateEstadoCultivo(Long id, EstadoCultivoRequest request) {
        EstadoCultivo estado = getEstadoEntity(id);
        Planta planta = getPlantaEntity(request.getPlantaId());

        estado.setPlanta(planta);
        estado.setFechaRegistro(request.getFechaRegistro());
        estado.setEstado(request.getEstado());
        estado.setAlturaCm(request.getAlturaCm());
        estado.setHumedadSustratoPct(request.getHumedadSustratoPct());
        estado.setTemperaturaC(request.getTemperaturaC());
        estado.setObservaciones(request.getObservaciones());

        EstadoCultivo saved = estadoCultivoRepository.save(estado);
        updateEstadoActual(planta, request.getEstado());
        return toResponse(saved);
    }

    /**
     * Retrieves a crop status record by id.
     *
     * @param id record id
     * @return record response
     */
    @Transactional(readOnly = true)
    public EstadoCultivoResponse getEstadoCultivoById(Long id) {
        return toResponse(getEstadoEntity(id));
    }

    /**
     * Retrieves all crop status records.
     *
     * @return list of crop status records
     */
    @Transactional(readOnly = true)
    public List<EstadoCultivoResponse> getAllEstadosCultivo() {
        return estadoCultivoRepository.findAll()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Deletes a crop status record by id.
     *
     * @param id record id
     */
    @Transactional
    public void deleteEstadoCultivo(Long id) {
        EstadoCultivo estado = getEstadoEntity(id);
        estadoCultivoRepository.delete(estado);
    }

    private EstadoCultivo getEstadoEntity(Long id) {
        return estadoCultivoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.estado_cultivo.no_encontrado"));
    }

    private Planta getPlantaEntity(Long id) {
        return plantaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.planta.no_encontrada"));
    }

    private void updateEstadoActual(Planta planta, com.greenhouse.manager.domain.enums.EstadoCultivoStatus estado) {
        planta.setEstadoActual(estado);
        plantaRepository.save(planta);
    }

    private EstadoCultivoResponse toResponse(EstadoCultivo estado) {
        EstadoCultivoResponse response = new EstadoCultivoResponse();
        response.setId(estado.getId());
        response.setPlantaId(estado.getPlanta().getId());
        response.setFechaRegistro(estado.getFechaRegistro());
        response.setEstado(estado.getEstado());
        response.setAlturaCm(estado.getAlturaCm());
        response.setHumedadSustratoPct(estado.getHumedadSustratoPct());
        response.setTemperaturaC(estado.getTemperaturaC());
        response.setObservaciones(estado.getObservaciones());
        response.setCreatedAt(estado.getCreatedAt());
        return response;
    }
}
