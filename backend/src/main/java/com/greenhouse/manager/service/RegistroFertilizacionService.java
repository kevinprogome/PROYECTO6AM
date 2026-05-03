/*
 * Proyecto: GreenHouse Manager
 * Archivo: RegistroFertilizacionService.java
 * Descripcion: Servicio de negocio para registros de fertilizacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.service;

import com.greenhouse.manager.api.dto.request.RegistroFertilizacionRequest;
import com.greenhouse.manager.api.dto.response.RegistroFertilizacionResponse;
import com.greenhouse.manager.api.exception.NotFoundException;
import com.greenhouse.manager.domain.entity.Planta;
import com.greenhouse.manager.domain.entity.RegistroFertilizacion;
import com.greenhouse.manager.domain.repository.PlantaRepository;
import com.greenhouse.manager.domain.repository.RegistroFertilizacionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing fertilization records.
 */
@Service
public class RegistroFertilizacionService {

    private final RegistroFertilizacionRepository registroFertilizacionRepository;
    private final PlantaRepository plantaRepository;

    /**
     * Creates a new RegistroFertilizacionService instance.
     *
     * @param registroFertilizacionRepository repository for fertilization records
     * @param plantaRepository repository for plants
     */
    public RegistroFertilizacionService(
        RegistroFertilizacionRepository registroFertilizacionRepository,
        PlantaRepository plantaRepository
    ) {
        this.registroFertilizacionRepository = registroFertilizacionRepository;
        this.plantaRepository = plantaRepository;
    }

    /**
     * Creates a fertilization record.
     *
     * @param request fertilization request
     * @return created record
     */
    @Transactional
    public RegistroFertilizacionResponse createRegistroFertilizacion(RegistroFertilizacionRequest request) {
        Planta planta = getPlantaEntity(request.getPlantaId());
        RegistroFertilizacion registro = new RegistroFertilizacion();
        registro.setPlanta(planta);
        registro.setFechaFertilizacion(request.getFechaFertilizacion());
        registro.setTipoFertilizante(request.getTipoFertilizante());
        registro.setDosis(request.getDosis());
        registro.setUnidad(request.getUnidad());
        registro.setResponsable(request.getResponsable());
        registro.setNotas(request.getNotas());

        RegistroFertilizacion saved = registroFertilizacionRepository.save(registro);
        updateFechaUltimaFertilizacion(planta, request.getFechaFertilizacion().toLocalDate());
        return toResponse(saved);
    }

    /**
     * Updates a fertilization record.
     *
     * @param id record id
     * @param request fertilization request
     * @return updated record
     */
    @Transactional
    public RegistroFertilizacionResponse updateRegistroFertilizacion(Long id, RegistroFertilizacionRequest request) {
        RegistroFertilizacion registro = getRegistroEntity(id);
        Planta planta = getPlantaEntity(request.getPlantaId());

        registro.setPlanta(planta);
        registro.setFechaFertilizacion(request.getFechaFertilizacion());
        registro.setTipoFertilizante(request.getTipoFertilizante());
        registro.setDosis(request.getDosis());
        registro.setUnidad(request.getUnidad());
        registro.setResponsable(request.getResponsable());
        registro.setNotas(request.getNotas());

        RegistroFertilizacion saved = registroFertilizacionRepository.save(registro);
        updateFechaUltimaFertilizacion(planta, request.getFechaFertilizacion().toLocalDate());
        return toResponse(saved);
    }

    /**
     * Retrieves a fertilization record by id.
     *
     * @param id record id
     * @return record response
     */
    @Transactional(readOnly = true)
    public RegistroFertilizacionResponse getRegistroFertilizacionById(Long id) {
        return toResponse(getRegistroEntity(id));
    }

    /**
     * Retrieves all fertilization records.
     *
     * @return list of fertilization records
     */
    @Transactional(readOnly = true)
    public List<RegistroFertilizacionResponse> getAllRegistrosFertilizacion() {
        return registroFertilizacionRepository.findAll()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Deletes a fertilization record by id.
     *
     * @param id record id
     */
    @Transactional
    public void deleteRegistroFertilizacion(Long id) {
        RegistroFertilizacion registro = getRegistroEntity(id);
        registroFertilizacionRepository.delete(registro);
    }

    private RegistroFertilizacion getRegistroEntity(Long id) {
        return registroFertilizacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.registro_fertilizacion.no_encontrado"));
    }

    private Planta getPlantaEntity(Long id) {
        return plantaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.planta.no_encontrada"));
    }

    private void updateFechaUltimaFertilizacion(Planta planta, LocalDate fechaUltimaFertilizacion) {
        planta.setFechaUltimaFertilizacion(fechaUltimaFertilizacion);
        plantaRepository.save(planta);
    }

    private RegistroFertilizacionResponse toResponse(RegistroFertilizacion registro) {
        RegistroFertilizacionResponse response = new RegistroFertilizacionResponse();
        response.setId(registro.getId());
        response.setPlantaId(registro.getPlanta().getId());
        response.setFechaFertilizacion(registro.getFechaFertilizacion());
        response.setTipoFertilizante(registro.getTipoFertilizante());
        response.setDosis(registro.getDosis());
        response.setUnidad(registro.getUnidad());
        response.setResponsable(registro.getResponsable());
        response.setNotas(registro.getNotas());
        response.setCreatedAt(registro.getCreatedAt());
        return response;
    }
}
