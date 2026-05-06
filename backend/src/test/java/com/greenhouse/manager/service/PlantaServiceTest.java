/*
 * Proyecto: GreenHouse Manager
 * Archivo: PlantaServiceTest.java
 * Descripcion: Pruebas unitarias del servicio de plantas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-06
 * Version: 1.0.0
 */
package com.greenhouse.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for PlantaService.
 */
@ExtendWith(MockitoExtension.class)
public class PlantaServiceTest {

    @Mock
    private PlantaRepository plantaRepository;

    @Mock
    private InvernaderoRepository invernaderoRepository;

    @InjectMocks
    private PlantaService plantaService;

    @Test
    void testCreatePlanta_guardaYMapeaResponse() {
        PlantaRequest request = buildRequest();
        Invernadero invernadero = buildInvernadero(request.getInvernaderoId());

        when(invernaderoRepository.findById(request.getInvernaderoId()))
            .thenReturn(Optional.of(invernadero));
        when(plantaRepository.save(any(Planta.class))).thenAnswer(invocation -> {
            Planta planta = invocation.getArgument(0);
            planta.setId(99L);
            return planta;
        });

        PlantaResponse response = plantaService.createPlanta(request);

        assertThat(response.getId()).isEqualTo(99L);
        assertThat(response.getInvernaderoId()).isEqualTo(invernadero.getId());
        assertThat(response.getNombreComun()).isEqualTo(request.getNombreComun());
        verify(plantaRepository).save(any(Planta.class));
    }

    @Test
    void testUpdatePlanta_actualizaCampos() {
        PlantaRequest request = buildRequest();
        request.setNombreComun("Nuevas Hierbas");
        Invernadero invernadero = buildInvernadero(request.getInvernaderoId());
        Planta existente = buildPlanta(10L, invernadero);

        when(plantaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(invernaderoRepository.findById(request.getInvernaderoId()))
            .thenReturn(Optional.of(invernadero));
        when(plantaRepository.save(existente)).thenReturn(existente);

        PlantaResponse response = plantaService.updatePlanta(10L, request);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getNombreComun()).isEqualTo("Nuevas Hierbas");
    }

    @Test
    void testGetPlantasQueNecesitanRiego_devuelveLista() {
        LocalDate fecha = LocalDate.of(2026, 5, 6);
        Invernadero invernadero = buildInvernadero(2L);
        Planta planta = buildPlanta(3L, invernadero);

        when(plantaRepository.findPlantasQueNecesitanRiego(fecha))
            .thenReturn(List.of(planta));

        List<PlantaResponse> respuesta = plantaService.getPlantasQueNecesitanRiego(fecha);

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).getId()).isEqualTo(3L);
        verify(plantaRepository).findPlantasQueNecesitanRiego(fecha);
    }

    @Test
    void testGetPlantasQueNecesitanFertilizacion_filtraActivas() {
        LocalDate fecha = LocalDate.of(2026, 5, 6);
        Invernadero invernadero = buildInvernadero(3L);

        Planta necesita = buildPlanta(1L, invernadero);
        necesita.setFechaUltimaFertilizacion(fecha.minusDays(10));
        necesita.setFrecuenciaFertilizacionDias(3);
        necesita.setActivo(true);

        Planta noNecesita = buildPlanta(2L, invernadero);
        noNecesita.setFechaUltimaFertilizacion(fecha);
        noNecesita.setFrecuenciaFertilizacionDias(10);
        noNecesita.setActivo(true);

        Planta inactiva = buildPlanta(3L, invernadero);
        inactiva.setFechaUltimaFertilizacion(fecha.minusDays(20));
        inactiva.setFrecuenciaFertilizacionDias(1);
        inactiva.setActivo(false);

        when(plantaRepository.findAll()).thenReturn(List.of(necesita, noNecesita, inactiva));

        List<PlantaResponse> respuesta = plantaService.getPlantasQueNecesitanFertilizacion(fecha);

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void testDeletePlanta_eliminaEntidad() {
        Invernadero invernadero = buildInvernadero(4L);
        Planta planta = buildPlanta(5L, invernadero);
        when(plantaRepository.findById(5L)).thenReturn(Optional.of(planta));

        plantaService.deletePlanta(5L);

        verify(plantaRepository).delete(planta);
    }

    @Test
    void testGetPlantaById_cuandoNoExiste_lanzaNotFound() {
        when(plantaRepository.findById(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> plantaService.getPlantaById(77L))
            .isInstanceOf(NotFoundException.class);
    }

    private PlantaRequest buildRequest() {
        PlantaRequest request = new PlantaRequest();
        request.setInvernaderoId(1L);
        request.setNombreComun("Tomate");
        request.setNombreCientifico("Solanum");
        request.setVariedad("Roma");
        request.setFechaSiembra(LocalDate.of(2026, 5, 1));
        request.setFechaUltimoRiego(LocalDate.of(2026, 5, 4));
        request.setFrecuenciaRiegoDias(2);
        request.setFechaUltimaFertilizacion(LocalDate.of(2026, 4, 28));
        request.setFrecuenciaFertilizacionDias(7);
        request.setEstadoActual(EstadoCultivoStatus.OPTIMO);
        request.setObservaciones("Ok");
        request.setActivo(true);
        return request;
    }

    private Invernadero buildInvernadero(Long id) {
        Invernadero invernadero = new Invernadero();
        invernadero.setId(id);
        invernadero.setNombre("Invernadero Test");
        invernadero.setUbicacion("Zona 1");
        return invernadero;
    }

    private Planta buildPlanta(Long id, Invernadero invernadero) {
        Planta planta = new Planta();
        planta.setId(id);
        planta.setInvernadero(invernadero);
        planta.setNombreComun("Planta Test");
        planta.setFrecuenciaRiegoDias(2);
        planta.setFrecuenciaFertilizacionDias(7);
        planta.setEstadoActual(EstadoCultivoStatus.OPTIMO);
        planta.setActivo(true);
        planta.setFechaUltimoRiego(LocalDate.of(2026, 5, 4));
        planta.setFechaUltimaFertilizacion(LocalDate.of(2026, 4, 28));
        return planta;
    }
}
