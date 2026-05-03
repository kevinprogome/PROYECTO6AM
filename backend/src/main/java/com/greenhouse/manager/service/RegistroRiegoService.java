/*
 * Proyecto: GreenHouse Manager
 * Archivo: RegistroRiegoService.java
 * Descripcion: Servicio de negocio para registros de riego.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.service;

import com.greenhouse.manager.api.dto.request.RegistroRiegoRequest;
import com.greenhouse.manager.api.dto.response.RegistroRiegoResponse;
import com.greenhouse.manager.api.exception.NotFoundException;
import com.greenhouse.manager.domain.entity.Planta;
import com.greenhouse.manager.domain.entity.RegistroRiego;
import com.greenhouse.manager.domain.repository.PlantaRepository;
import com.greenhouse.manager.domain.repository.RegistroRiegoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing irrigation records.
 */
@Service
public class RegistroRiegoService {

    private final RegistroRiegoRepository registroRiegoRepository;
    private final PlantaRepository plantaRepository;

    /**
     * Creates a new RegistroRiegoService instance.
     *
     * @param registroRiegoRepository repository for irrigation records
     * @param plantaRepository repository for plants
     */
    public RegistroRiegoService(
        RegistroRiegoRepository registroRiegoRepository,
        PlantaRepository plantaRepository
    ) {
        this.registroRiegoRepository = registroRiegoRepository;
        this.plantaRepository = plantaRepository;
    }

    /**
     * Creates an irrigation record.
     *
     * @param request irrigation request
     * @return created record
     */
    @Transactional
    public RegistroRiegoResponse createRegistroRiego(RegistroRiegoRequest request) {
        Planta planta = getPlantaEntity(request.getPlantaId());
        RegistroRiego registro = new RegistroRiego();
        registro.setPlanta(planta);
        registro.setFechaRiego(request.getFechaRiego());
        registro.setVolumenLitros(request.getVolumenLitros());
        registro.setMetodo(request.getMetodo());
        registro.setResponsable(request.getResponsable());
        registro.setNotas(request.getNotas());

        RegistroRiego saved = registroRiegoRepository.save(registro);
        updateFechaUltimoRiego(planta, request.getFechaRiego().toLocalDate());
        return toResponse(saved);
    }

    /**
     * Updates an irrigation record.
     *
     * @param id record id
     * @param request irrigation request
     * @return updated record
     */
    @Transactional
    public RegistroRiegoResponse updateRegistroRiego(Long id, RegistroRiegoRequest request) {
        RegistroRiego registro = getRegistroEntity(id);
        Planta planta = getPlantaEntity(request.getPlantaId());

        registro.setPlanta(planta);
        registro.setFechaRiego(request.getFechaRiego());
        registro.setVolumenLitros(request.getVolumenLitros());
        registro.setMetodo(request.getMetodo());
        registro.setResponsable(request.getResponsable());
        registro.setNotas(request.getNotas());

        RegistroRiego saved = registroRiegoRepository.save(registro);
        updateFechaUltimoRiego(planta, request.getFechaRiego().toLocalDate());
        return toResponse(saved);
    }

    /**
     * Retrieves an irrigation record by id.
     *
     * @param id record id
     * @return record response
     */
    @Transactional(readOnly = true)
    public RegistroRiegoResponse getRegistroRiegoById(Long id) {
        return toResponse(getRegistroEntity(id));
    }

    /**
     * Retrieves all irrigation records.
     *
     * @return list of irrigation records
     */
    @Transactional(readOnly = true)
    public List<RegistroRiegoResponse> getAllRegistrosRiego() {
        return registroRiegoRepository.findAll()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Deletes an irrigation record by id.
     *
     * @param id record id
     */
    @Transactional
    public void deleteRegistroRiego(Long id) {
        RegistroRiego registro = getRegistroEntity(id);
        registroRiegoRepository.delete(registro);
    }

    private RegistroRiego getRegistroEntity(Long id) {
        return registroRiegoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.registro_riego.no_encontrado"));
    }

    private Planta getPlantaEntity(Long id) {
        return plantaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.planta.no_encontrada"));
    }

    private void updateFechaUltimoRiego(Planta planta, LocalDate fechaUltimoRiego) {
        planta.setFechaUltimoRiego(fechaUltimoRiego);
        plantaRepository.save(planta);
    }

    private RegistroRiegoResponse toResponse(RegistroRiego registro) {
        RegistroRiegoResponse response = new RegistroRiegoResponse();
        response.setId(registro.getId());
        response.setPlantaId(registro.getPlanta().getId());
        response.setFechaRiego(registro.getFechaRiego());
        response.setVolumenLitros(registro.getVolumenLitros());
        response.setMetodo(registro.getMetodo());
        response.setResponsable(registro.getResponsable());
        response.setNotas(registro.getNotas());
        response.setCreatedAt(registro.getCreatedAt());
        return response;
    }
}
