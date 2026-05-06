/*
 * Proyecto: GreenHouse Manager
 * Archivo: InvernaderoControllerTest.java
 * Descripcion: Pruebas MVC para el controlador de invernaderos.
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
import com.greenhouse.manager.api.dto.request.InvernaderoRequest;
import com.greenhouse.manager.api.dto.response.InvernaderoResponse;
import com.greenhouse.manager.api.dto.response.InvernaderoStatsResponse;
import com.greenhouse.manager.api.exception.NotFoundException;
import com.greenhouse.manager.service.InvernaderoService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * MVC tests for InvernaderoController.
 */
@WebMvcTest(InvernaderoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InvernaderoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InvernaderoService invernaderoService;

    @Test
    void testGetAllInvernaderos_retornaLista() throws Exception {
        when(invernaderoService.getAllInvernaderos()).thenReturn(List.of(buildResponse(1L)));

        mockMvc.perform(get("/api/invernaderos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testGetInvernaderoById_retornaElemento() throws Exception {
        when(invernaderoService.getInvernaderoById(1L)).thenReturn(buildResponse(1L));

        mockMvc.perform(get("/api/invernaderos/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetInvernaderoById_notFound() throws Exception {
        when(invernaderoService.getInvernaderoById(99L))
            .thenThrow(new NotFoundException("error.invernadero.no_encontrado"));

        mockMvc.perform(get("/api/invernaderos/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCreateInvernadero_creaElemento() throws Exception {
        InvernaderoRequest request = buildRequest();
        when(invernaderoService.createInvernadero(any(InvernaderoRequest.class)))
            .thenReturn(buildResponse(10L));

        mockMvc.perform(post("/api/invernaderos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void testCreateInvernadero_invalido() throws Exception {
        InvernaderoRequest request = new InvernaderoRequest();
        request.setNombre("");

        mockMvc.perform(post("/api/invernaderos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(invernaderoService, never()).createInvernadero(any(InvernaderoRequest.class));
    }

    @Test
    void testUpdateInvernadero_actualizaElemento() throws Exception {
        InvernaderoRequest request = buildRequest();
        when(invernaderoService.updateInvernadero(eq(1L), any(InvernaderoRequest.class)))
            .thenReturn(buildResponse(1L));

        mockMvc.perform(put("/api/invernaderos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testDeleteInvernadero_noContent() throws Exception {
        doNothing().when(invernaderoService).deleteInvernadero(1L);

        mockMvc.perform(delete("/api/invernaderos/1"))
            .andExpect(status().isNoContent());

        verify(invernaderoService).deleteInvernadero(1L);
    }

    @Test
    void testGetInvernaderoStats_ok() throws Exception {
        when(invernaderoService.getInvernaderoStats(1L)).thenReturn(buildStats(1L));

        mockMvc.perform(get("/api/invernaderos/1/stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.invernaderoId").value(1L))
            .andExpect(jsonPath("$.totalPlantas").value(3));
    }

    private InvernaderoRequest buildRequest() {
        InvernaderoRequest request = new InvernaderoRequest();
        request.setUsuarioId(7L);
        request.setNombre("Invernadero Norte");
        request.setUbicacion("Lote 1");
        request.setDescripcion("Principal");
        request.setAreaM2(new BigDecimal("25.5"));
        return request;
    }

    private InvernaderoResponse buildResponse(Long id) {
        InvernaderoResponse response = new InvernaderoResponse();
        response.setId(id);
        response.setUsuarioId(7L);
        response.setNombre("Invernadero Norte");
        response.setUbicacion("Lote 1");
        response.setDescripcion("Principal");
        response.setAreaM2(new BigDecimal("25.5"));
        return response;
    }

    private InvernaderoStatsResponse buildStats(Long id) {
        InvernaderoStatsResponse response = new InvernaderoStatsResponse();
        response.setInvernaderoId(id);
        response.setTotalPlantas(3);
        response.setAlertasActivas(1);
        response.setPlantasNecesitanRiego(2);
        response.setPlantasNecesitanFertilizacion(1);
        return response;
    }
}
