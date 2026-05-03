/*
 * Proyecto: GreenHouse Manager
 * Archivo: InvernaderoService.java
 * Descripcion: Servicio de negocio para invernaderos.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.service;

import com.greenhouse.manager.api.dto.request.InvernaderoRequest;
import com.greenhouse.manager.api.dto.response.InvernaderoResponse;
import com.greenhouse.manager.api.dto.response.InvernaderoStatsResponse;
import com.greenhouse.manager.api.exception.NotFoundException;
import com.greenhouse.manager.domain.entity.Invernadero;
import com.greenhouse.manager.domain.entity.Planta;
import com.greenhouse.manager.domain.entity.Usuario;
import com.greenhouse.manager.domain.repository.AlertaRepository;
import com.greenhouse.manager.domain.repository.InvernaderoRepository;
import com.greenhouse.manager.domain.repository.UsuarioRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing greenhouses.
 */
@Service
public class InvernaderoService {

    private final InvernaderoRepository invernaderoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlertaRepository alertaRepository;

    /**
     * Creates a new InvernaderoService instance.
     *
     * @param invernaderoRepository repository for greenhouses
     * @param usuarioRepository repository for users
     * @param alertaRepository repository for alerts
     */
    public InvernaderoService(
        InvernaderoRepository invernaderoRepository,
        UsuarioRepository usuarioRepository,
        AlertaRepository alertaRepository
    ) {
        this.invernaderoRepository = invernaderoRepository;
        this.usuarioRepository = usuarioRepository;
        this.alertaRepository = alertaRepository;
    }

    /**
     * Creates a greenhouse.
     *
     * @param request greenhouse request
     * @return created greenhouse
     */
    @Transactional
    public InvernaderoResponse createInvernadero(InvernaderoRequest request) {
        Invernadero invernadero = new Invernadero();
        applyRequest(invernadero, request);
        Invernadero saved = invernaderoRepository.save(invernadero);
        return toResponse(saved);
    }

    /**
     * Updates a greenhouse.
     *
     * @param id greenhouse id
     * @param request greenhouse request
     * @return updated greenhouse
     */
    @Transactional
    public InvernaderoResponse updateInvernadero(Long id, InvernaderoRequest request) {
        Invernadero invernadero = getInvernaderoEntity(id);
        applyRequest(invernadero, request);
        Invernadero saved = invernaderoRepository.save(invernadero);
        return toResponse(saved);
    }

    /**
     * Retrieves a greenhouse by id.
     *
     * @param id greenhouse id
     * @return greenhouse response
     */
    @Transactional(readOnly = true)
    public InvernaderoResponse getInvernaderoById(Long id) {
        return toResponse(getInvernaderoEntity(id));
    }

    /**
     * Retrieves all greenhouses.
     *
     * @return list of greenhouses
     */
    @Transactional(readOnly = true)
    public List<InvernaderoResponse> getAllInvernaderos() {
        return invernaderoRepository.findAll()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Deletes a greenhouse by id.
     *
     * @param id greenhouse id
     */
    @Transactional
    public void deleteInvernadero(Long id) {
        Invernadero invernadero = getInvernaderoEntity(id);
        invernaderoRepository.delete(invernadero);
    }

    /**
     * Retrieves greenhouse statistics.
     *
     * @param id greenhouse id
     * @return greenhouse statistics
     */
    @Transactional(readOnly = true)
    public InvernaderoStatsResponse getInvernaderoStats(Long id) {
        Invernadero invernadero = getInvernaderoEntity(id);
        List<Planta> plantas = invernadero.getPlantas();
        LocalDate hoy = LocalDate.now();

        int totalPlantas = plantas.size();
        int plantasNecesitanRiego = 0;
        int plantasNecesitanFertilizacion = 0;

        for (Planta planta : plantas) {
            if (!Boolean.TRUE.equals(planta.getActivo())) {
                continue;
            }
            if (necesitaRiego(planta, hoy)) {
                plantasNecesitanRiego++;
            }
            if (necesitaFertilizacion(planta, hoy)) {
                plantasNecesitanFertilizacion++;
            }
        }

        int alertasActivas = alertaRepository.findAlertasActivasPorInvernadero(id).size();

        InvernaderoStatsResponse response = new InvernaderoStatsResponse();
        response.setInvernaderoId(id);
        response.setTotalPlantas(totalPlantas);
        response.setAlertasActivas(alertasActivas);
        response.setPlantasNecesitanRiego(plantasNecesitanRiego);
        response.setPlantasNecesitanFertilizacion(plantasNecesitanFertilizacion);
        return response;
    }

    private Invernadero getInvernaderoEntity(Long id) {
        return invernaderoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.invernadero.no_encontrado"));
    }

    private Usuario getUsuarioEntity(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.usuario.no_encontrado"));
    }

    private void applyRequest(Invernadero invernadero, InvernaderoRequest request) {
        invernadero.setUsuario(getUsuarioEntity(request.getUsuarioId()));
        invernadero.setNombre(request.getNombre());
        invernadero.setUbicacion(request.getUbicacion());
        invernadero.setDescripcion(request.getDescripcion());
        invernadero.setAreaM2(request.getAreaM2());
    }

    private InvernaderoResponse toResponse(Invernadero invernadero) {
        InvernaderoResponse response = new InvernaderoResponse();
        response.setId(invernadero.getId());
        response.setUsuarioId(invernadero.getUsuario().getId());
        response.setNombre(invernadero.getNombre());
        response.setUbicacion(invernadero.getUbicacion());
        response.setDescripcion(invernadero.getDescripcion());
        response.setAreaM2(invernadero.getAreaM2());
        response.setCreatedAt(invernadero.getCreatedAt());
        response.setUpdatedAt(invernadero.getUpdatedAt());
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
