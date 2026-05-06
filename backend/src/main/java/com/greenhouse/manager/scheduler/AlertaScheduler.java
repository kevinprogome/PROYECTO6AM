/*
 * Proyecto: GreenHouse Manager
 * Archivo: AlertaScheduler.java
 * Descripcion: Scheduler que genera alertas automaticas de riego, fertilizacion y estado.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-06
 * Version: 1.0.0
 */
package com.greenhouse.manager.scheduler;

import com.greenhouse.manager.domain.entity.Alerta;
import com.greenhouse.manager.domain.entity.Invernadero;
import com.greenhouse.manager.domain.entity.Planta;
import com.greenhouse.manager.domain.enums.AlertaSeveridad;
import com.greenhouse.manager.domain.enums.AlertaTipo;
import com.greenhouse.manager.domain.enums.EstadoCultivoStatus;
import com.greenhouse.manager.domain.repository.AlertaRepository;
import com.greenhouse.manager.domain.repository.PlantaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduler that generates daily alerts based on plant status and schedules.
 */
@Component
public class AlertaScheduler {

    private static final Logger log = LoggerFactory.getLogger(AlertaScheduler.class);
    private static final Locale LOCALE_ES = new Locale("es", "ES");
    private static final Locale LOCALE_EN = Locale.ENGLISH;

    private final PlantaRepository plantaRepository;
    private final AlertaRepository alertaRepository;
    private final MessageSource messageSource;

    /**
     * Creates a new scheduler instance.
     *
     * @param plantaRepository repository for plants
     * @param alertaRepository repository for alerts
     * @param messageSource message source for i18n logging
     */
    public AlertaScheduler(
        PlantaRepository plantaRepository,
        AlertaRepository alertaRepository,
        MessageSource messageSource
    ) {
        this.plantaRepository = plantaRepository;
        this.alertaRepository = alertaRepository;
        this.messageSource = messageSource;
    }

    /**
     * Runs every day at 8:00 AM to generate alerts.
     */
    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void generarAlertasDiarias() {
        generarAlertasAutomaticas(LocalDate.now());
    }

    /**
     * Generates automatic alerts for a specific date.
     *
     * @param fechaActual date to evaluate
     * @return map of greenhouse id to generated alert count
     */
    @Transactional
    public Map<Long, Integer> generarAlertasAutomaticas(LocalDate fechaActual) {
        Map<Long, Integer> conteoPorInvernadero = new HashMap<>();
        Map<Long, String> nombresInvernadero = new HashMap<>();
        LocalDateTime inicioDia = fechaActual.atStartOfDay();
        LocalDateTime finDia = fechaActual.plusDays(1).atStartOfDay().minusNanos(1);

        for (Planta planta : plantaRepository.findAll()) {
            if (!Boolean.TRUE.equals(planta.getActivo())) {
                continue;
            }
            Invernadero invernadero = planta.getInvernadero();
            if (invernadero == null || invernadero.getId() == null) {
                continue;
            }
            nombresInvernadero.putIfAbsent(
                invernadero.getId(),
                invernadero.getNombre() == null ? String.valueOf(invernadero.getId()) : invernadero.getNombre()
            );

            if (necesitaRiego(planta, fechaActual)
                && !existeAlertaDiaria(planta.getId(), AlertaTipo.RIEGO, inicioDia, finDia)) {
                registrarAlerta(planta, AlertaTipo.RIEGO, AlertaSeveridad.MEDIA, "alerta.riego.pendiente");
                conteoPorInvernadero.merge(invernadero.getId(), 1, Integer::sum);
            }

            if (necesitaFertilizacion(planta, fechaActual)
                && !existeAlertaDiaria(planta.getId(), AlertaTipo.FERTILIZACION, inicioDia, finDia)) {
                registrarAlerta(planta, AlertaTipo.FERTILIZACION, AlertaSeveridad.MEDIA,
                    "alerta.fertilizacion.pendiente");
                conteoPorInvernadero.merge(invernadero.getId(), 1, Integer::sum);
            }

            if (EstadoCultivoStatus.CRITICO == planta.getEstadoActual()
                && !existeAlertaDiaria(planta.getId(), AlertaTipo.ESTADO, inicioDia, finDia)) {
                registrarAlerta(planta, AlertaTipo.ESTADO, AlertaSeveridad.ALTA, "alerta.estado.critico");
                conteoPorInvernadero.merge(invernadero.getId(), 1, Integer::sum);
            }
        }

        logResultados(conteoPorInvernadero, nombresInvernadero, fechaActual);
        return conteoPorInvernadero;
    }

    private void registrarAlerta(
        Planta planta,
        AlertaTipo tipo,
        AlertaSeveridad severidad,
        String mensaje
    ) {
        Alerta alerta = new Alerta();
        alerta.setPlanta(planta);
        alerta.setInvernadero(planta.getInvernadero());
        alerta.setTipo(tipo);
        alerta.setSeveridad(severidad);
        alerta.setMensaje(mensaje);
        alerta.setActiva(true);
        alerta.setFechaGeneracion(LocalDateTime.now());
        alertaRepository.save(alerta);
    }

    private boolean existeAlertaDiaria(
        Long plantaId,
        AlertaTipo tipo,
        LocalDateTime inicio,
        LocalDateTime fin
    ) {
        return alertaRepository.existsByPlantaIdAndTipoAndFechaGeneracionBetween(plantaId, tipo, inicio, fin);
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

    private void logResultados(
        Map<Long, Integer> conteo,
        Map<Long, String> nombres,
        LocalDate fechaActual
    ) {
        if (conteo.isEmpty()) {
            logInfoMessage("log.alertas.sin_generar", new Object[] { fechaActual });
            return;
        }
        conteo.forEach((invernaderoId, total) -> {
            String nombre = nombres.getOrDefault(invernaderoId, String.valueOf(invernaderoId));
            logInfoMessage("log.alertas.generadas", new Object[] { total, nombre });
        });
    }

    private void logInfoMessage(String key, Object[] args) {
        String mensajeEs = messageSource.getMessage(key, args, LOCALE_ES);
        String mensajeEn = messageSource.getMessage(key, args, LOCALE_EN);
        log.info(mensajeEs);
        log.info(mensajeEn);
    }
}
