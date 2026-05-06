/*
 * Proyecto: GreenHouse Manager
 * Archivo: PlantaControllerTest.java
 * Descripcion: Pruebas MVC para el controlador de plantas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-06
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenhouse.manager.api.dto.request.PlantaRequest;
import com.greenhouse.manager.api.dto.response.PlantaResponse;
import com.greenhouse.manager.api.exception.NotFoundException;
import com.greenhouse.manager.domain.enums.EstadoCultivoStatus;
import com.greenhouse.manager.service.PlantaService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * MVC tests for PlantaController.
 */
@WebMvcTest(PlantaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PlantaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlantaService plantaService;

    @Test
    void testGetAllPlantas_retornaLista() throws Exception {
        when(plantaService.getAllPlantas()).thenReturn(List.of(buildResponse(1L)));

        mockMvc.perform(get("/api/plantas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testGetPlantaById_retornaElemento() throws Exception {
        when(plantaService.getPlantaById(1L)).thenReturn(buildResponse(1L));

        mockMvc.perform(get("/api/plantas/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetPlantaById_notFound() throws Exception {
        when(plantaService.getPlantaById(99L))
            .thenThrow(new NotFoundException("error.planta.no_encontrada"));

        mockMvc.perform(get("/api/plantas/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePlanta_creaElemento() throws Exception {
        PlantaRequest request = buildRequest();
        when(plantaService.createPlanta(any(PlantaRequest.class))).thenReturn(buildResponse(10L));

        mockMvc.perform(post("/api/plantas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void testCreatePlanta_invalida() throws Exception {
        PlantaRequest request = new PlantaRequest();
        request.setNombreComun("");

        mockMvc.perform(post("/api/plantas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(plantaService, never()).createPlanta(any(PlantaRequest.class));
    }

    @Test
    void testUpdatePlanta_actualizaElemento() throws Exception {
        PlantaRequest request = buildRequest();
        when(plantaService.updatePlanta(eq(1L), any(PlantaRequest.class)))
            .thenReturn(buildResponse(1L));

        mockMvc.perform(put("/api/plantas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testDeletePlanta_noContent() throws Exception {
        doNothing().when(plantaService).deletePlanta(1L);

        mockMvc.perform(delete("/api/plantas/1"))
            .andExpect(status().isNoContent());

        verify(plantaService).deletePlanta(1L);
    }

    @Test
    void testGetPlantasByEstado_ok() throws Exception {
        when(plantaService.getPlantasByEstado(EstadoCultivoStatus.OPTIMO))
            .thenReturn(List.of(buildResponse(2L)));

        mockMvc.perform(get("/api/plantas/estado/OPTIMO"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(2L));
    }

    @Test
    void testGetPlantasQueNecesitanRiego_ok() throws Exception {
        LocalDate fecha = LocalDate.of(2026, 5, 6);
        when(plantaService.getPlantasQueNecesitanRiego(fecha))
            .thenReturn(List.of(buildResponse(3L)));

        mockMvc.perform(get("/api/plantas/necesitan-riego")
                .param("fecha", "2026-05-06"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(3L));
    }

    @Test
    void testGetPlantasQueNecesitanFertilizacion_ok() throws Exception {
        LocalDate fecha = LocalDate.of(2026, 5, 6);
        when(plantaService.getPlantasQueNecesitanFertilizacion(fecha))
            .thenReturn(List.of(buildResponse(4L)));

        mockMvc.perform(get("/api/plantas/necesitan-fertilizacion")
                .param("fecha", "2026-05-06"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(4L));
    }

    private PlantaRequest buildRequest() {
        PlantaRequest request = new PlantaRequest();
        request.setInvernaderoId(5L);
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

    private PlantaResponse buildResponse(Long id) {
        PlantaResponse response = new PlantaResponse();
        response.setId(id);
        response.setInvernaderoId(5L);
        response.setNombreComun("Tomate");
        response.setVariedad("Roma");
        response.setFrecuenciaRiegoDias(2);
        response.setFrecuenciaFertilizacionDias(7);
        response.setEstadoActual(EstadoCultivoStatus.OPTIMO);
        response.setActivo(true);
        return response;
    }
}
