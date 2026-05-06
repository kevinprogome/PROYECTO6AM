/*
 * Proyecto: GreenHouse Manager
 * Archivo: AlertaService.java
 * Descripcion: Servicio de negocio para alertas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.service;

import com.greenhouse.manager.api.dto.request.AlertaRequest;
import com.greenhouse.manager.api.dto.response.AlertaResponse;
import com.greenhouse.manager.api.exception.NotFoundException;
import com.greenhouse.manager.domain.entity.Alerta;
import com.greenhouse.manager.domain.entity.Invernadero;
import com.greenhouse.manager.domain.entity.Planta;
import com.greenhouse.manager.domain.entity.Usuario;
import com.greenhouse.manager.domain.repository.AlertaRepository;
import com.greenhouse.manager.domain.repository.InvernaderoRepository;
import com.greenhouse.manager.domain.repository.PlantaRepository;
import com.greenhouse.manager.domain.repository.UsuarioRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing alerts.
 */
@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final PlantaRepository plantaRepository;
    private final InvernaderoRepository invernaderoRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Creates a new AlertaService instance.
     *
     * @param alertaRepository repository for alerts
     * @param plantaRepository repository for plants
     * @param invernaderoRepository repository for greenhouses
     * @param usuarioRepository repository for users
     */
    public AlertaService(
        AlertaRepository alertaRepository,
        PlantaRepository plantaRepository,
        InvernaderoRepository invernaderoRepository,
        UsuarioRepository usuarioRepository
    ) {
        this.alertaRepository = alertaRepository;
        this.plantaRepository = plantaRepository;
        this.invernaderoRepository = invernaderoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Creates an alert.
     *
     * @param request alert request
     * @return created alert
     */
    @Transactional
    public AlertaResponse createAlerta(AlertaRequest request) {
        Alerta alerta = new Alerta();
        applyRequest(alerta, request, true);
        Alerta saved = alertaRepository.save(alerta);
        return toResponse(saved);
    }

    /**
     * Updates an alert.
     *
     * @param id alert id
     * @param request alert request
     * @return updated alert
     */
    @Transactional
    public AlertaResponse updateAlerta(Long id, AlertaRequest request) {
        Alerta alerta = getAlertaEntity(id);
        applyRequest(alerta, request, false);
        Alerta saved = alertaRepository.save(alerta);
        return toResponse(saved);
    }

    /**
     * Retrieves an alert by id.
     *
     * @param id alert id
     * @return alert response
     */
    @Transactional(readOnly = true)
    public AlertaResponse getAlertaById(Long id) {
        return toResponse(getAlertaEntity(id));
    }

    /**
     * Retrieves all alerts.
     *
     * @return list of alerts
     */
    @Transactional(readOnly = true)
    public List<AlertaResponse> getAllAlertas() {
        return alertaRepository.findAll()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves active alerts for a greenhouse.
     *
     * @param invernaderoId greenhouse id
     * @return list of active alerts
     */
    @Transactional(readOnly = true)
    public List<AlertaResponse> getAlertasActivasPorInvernadero(Long invernaderoId) {
        return alertaRepository.findAlertasActivasPorInvernadero(invernaderoId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Deletes an alert by id.
     *
     * @param id alert id
     */
    @Transactional
    public void deleteAlerta(Long id) {
        Alerta alerta = getAlertaEntity(id);
        alertaRepository.delete(alerta);
    }

    private Alerta getAlertaEntity(Long id) {
        return alertaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.alerta.no_encontrada"));
    }

    private Planta getPlantaEntity(Long id) {
        return plantaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.planta.no_encontrada"));
    }

    private Invernadero getInvernaderoEntity(Long id) {
        return invernaderoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.invernadero.no_encontrado"));
    }

    private Usuario getUsuarioEntity(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("error.usuario.no_encontrado"));
    }

    private void applyRequest(Alerta alerta, AlertaRequest request, boolean isNew) {
        alerta.setPlanta(getPlantaEntity(request.getPlantaId()));
        alerta.setInvernadero(getInvernaderoEntity(request.getInvernaderoId()));
        alerta.setTipo(request.getTipo());
        alerta.setSeveridad(request.getSeveridad());
        alerta.setMensaje(request.getMensaje());
        alerta.setActiva(request.getActiva());

        if (request.getFechaGeneracion() != null || isNew) {
            alerta.setFechaGeneracion(request.getFechaGeneracion());
        }
        if (request.getFechaResolucion() != null) {
            alerta.setFechaResolucion(request.getFechaResolucion());
        }
        if (request.getResueltaPorUsuarioId() != null) {
            alerta.setResueltaPorUsuario(getUsuarioEntity(request.getResueltaPorUsuarioId()));
        } else {
            alerta.setResueltaPorUsuario(null);
        }
    }

    private AlertaResponse toResponse(Alerta alerta) {
        AlertaResponse response = new AlertaResponse();
        response.setId(alerta.getId());
        response.setPlantaId(alerta.getPlanta().getId());
        response.setInvernaderoId(alerta.getInvernadero().getId());
        response.setTipo(alerta.getTipo());
        response.setSeveridad(alerta.getSeveridad());
        response.setMensaje(alerta.getMensaje());
        response.setActiva(alerta.getActiva());
        response.setFechaGeneracion(alerta.getFechaGeneracion());
        response.setFechaResolucion(alerta.getFechaResolucion());
        if (alerta.getResueltaPorUsuario() != null) {
            response.setResueltaPorUsuarioId(alerta.getResueltaPorUsuario().getId());
        }
        return response;
    }
}
