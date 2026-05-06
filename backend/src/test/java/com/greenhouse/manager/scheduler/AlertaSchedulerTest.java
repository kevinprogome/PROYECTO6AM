/*
 * Proyecto: GreenHouse Manager
 * Archivo: AlertaSchedulerTest.java
 * Descripcion: Pruebas unitarias del scheduler de alertas automaticas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-06
 * Version: 1.0.0
 */
package com.greenhouse.manager.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.greenhouse.manager.domain.entity.Alerta;
import com.greenhouse.manager.domain.entity.Invernadero;
import com.greenhouse.manager.domain.entity.Planta;
import com.greenhouse.manager.domain.entity.Usuario;
import com.greenhouse.manager.domain.enums.AlertaSeveridad;
import com.greenhouse.manager.domain.enums.AlertaTipo;
import com.greenhouse.manager.domain.enums.AuthProvider;
import com.greenhouse.manager.domain.enums.EstadoCultivoStatus;
import com.greenhouse.manager.domain.enums.UserRole;
import com.greenhouse.manager.domain.repository.AlertaRepository;
import com.greenhouse.manager.domain.repository.PlantaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;

/**
 * Tests for AlertaScheduler alert generation logic.
 */
@SpringBootTest(classes = AlertaScheduler.class)
@ActiveProfiles("test")
public class AlertaSchedulerTest {

    @Autowired
    private AlertaScheduler scheduler;

    @MockBean
    private PlantaRepository plantaRepository;

    @MockBean
    private AlertaRepository alertaRepository;

    @MockBean
    private MessageSource messageSource;

    private Planta planta;
    private Invernadero invernadero;

    /**
     * Sets up common entities and mocks before each test.
     */
    @BeforeEach
    void setUp() {
        Usuario usuario = new Usuario();
        usuario.setId(10L);
        usuario.setNombre("Operador Test");
        usuario.setEmail("operador@test.com");
        usuario.setRol(UserRole.OPERADOR);
        usuario.setProvider(AuthProvider.GOOGLE);
        usuario.setProviderId("google");
        usuario.setActivo(true);

        invernadero = new Invernadero();
        invernadero.setId(1L);
        invernadero.setNombre("Invernadero Norte");
        invernadero.setUbicacion("Lote 2");
        invernadero.setUsuario(usuario);

        planta = new Planta();
        planta.setId(100L);
        planta.setInvernadero(invernadero);
        planta.setNombreComun("Tomate");
        planta.setFrecuenciaRiegoDias(1);
        planta.setFrecuenciaFertilizacionDias(7);
        planta.setEstadoActual(EstadoCultivoStatus.OPTIMO);
        planta.setActivo(true);

        when(alertaRepository.save(any(Alerta.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(messageSource.getMessage(anyString(), any(Object[].class), any(Locale.class)))
            .thenReturn("msg");
    }

    /**
     * Valida que se genere alerta de riego cuando la fecha ya vencio.
     */
    @Test
    void testGenerarAlertasRiego_cuandoFechaVencida() {
        LocalDate hoy = LocalDate.of(2026, 5, 6);
        planta.setFechaUltimoRiego(hoy.minusDays(2));
        planta.setFechaUltimaFertilizacion(hoy);

        when(plantaRepository.findAll()).thenReturn(List.of(planta));
        when(alertaRepository.existsByPlantaIdAndTipoAndFechaGeneracionBetween(
            eq(planta.getId()), eq(AlertaTipo.RIEGO), any(), any()))
            .thenReturn(false);
        when(alertaRepository.existsByPlantaIdAndTipoAndFechaGeneracionBetween(
            eq(planta.getId()), eq(AlertaTipo.FERTILIZACION), any(), any()))
            .thenReturn(true);
        when(alertaRepository.existsByPlantaIdAndTipoAndFechaGeneracionBetween(
            eq(planta.getId()), eq(AlertaTipo.ESTADO), any(), any()))
            .thenReturn(true);

        Map<Long, Integer> resultado = scheduler.generarAlertasAutomaticas(hoy);

        assertThat(resultado.get(invernadero.getId())).isEqualTo(1);
        ArgumentCaptor<Alerta> captor = ArgumentCaptor.forClass(Alerta.class);
        verify(alertaRepository).save(captor.capture());
        assertThat(captor.getValue().getTipo()).isEqualTo(AlertaTipo.RIEGO);
        assertThat(captor.getValue().getSeveridad()).isEqualTo(AlertaSeveridad.MEDIA);
    }

    /**
     * Valida que no se genere alerta duplicada si ya existe una del mismo dia.
     */
    @Test
    void testNoGenerarAlertaDuplicada_mismoDia() {
        LocalDate hoy = LocalDate.of(2026, 5, 6);
        planta.setFechaUltimoRiego(hoy.minusDays(1));
        planta.setFechaUltimaFertilizacion(hoy);

        when(plantaRepository.findAll()).thenReturn(List.of(planta));
        when(alertaRepository.existsByPlantaIdAndTipoAndFechaGeneracionBetween(
            eq(planta.getId()), eq(AlertaTipo.RIEGO), any(), any()))
            .thenReturn(true);
        when(alertaRepository.existsByPlantaIdAndTipoAndFechaGeneracionBetween(
            eq(planta.getId()), eq(AlertaTipo.FERTILIZACION), any(), any()))
            .thenReturn(true);
        when(alertaRepository.existsByPlantaIdAndTipoAndFechaGeneracionBetween(
            eq(planta.getId()), eq(AlertaTipo.ESTADO), any(), any()))
            .thenReturn(true);

        Map<Long, Integer> resultado = scheduler.generarAlertasAutomaticas(hoy);

        assertThat(resultado.containsKey(invernadero.getId())).isFalse();
        verify(alertaRepository, never()).save(any(Alerta.class));
    }

    /**
     * Valida que se genere alerta critica cuando el estado del cultivo es CRITICO.
     */
    @Test
    void testGenerarAlertaCritica_estadoCritico() {
        LocalDate hoy = LocalDate.of(2026, 5, 6);
        planta.setEstadoActual(EstadoCultivoStatus.CRITICO);
        planta.setFechaUltimoRiego(hoy);
        planta.setFechaUltimaFertilizacion(hoy);

        when(plantaRepository.findAll()).thenReturn(List.of(planta));
        when(alertaRepository.existsByPlantaIdAndTipoAndFechaGeneracionBetween(
            eq(planta.getId()), eq(AlertaTipo.ESTADO), any(), any()))
            .thenReturn(false);
        when(alertaRepository.existsByPlantaIdAndTipoAndFechaGeneracionBetween(
            eq(planta.getId()), eq(AlertaTipo.RIEGO), any(), any()))
            .thenReturn(true);
        when(alertaRepository.existsByPlantaIdAndTipoAndFechaGeneracionBetween(
            eq(planta.getId()), eq(AlertaTipo.FERTILIZACION), any(), any()))
            .thenReturn(true);

        Map<Long, Integer> resultado = scheduler.generarAlertasAutomaticas(hoy);

        assertThat(resultado.get(invernadero.getId())).isEqualTo(1);
        ArgumentCaptor<Alerta> captor = ArgumentCaptor.forClass(Alerta.class);
        verify(alertaRepository).save(captor.capture());
        assertThat(captor.getValue().getTipo()).isEqualTo(AlertaTipo.ESTADO);
        assertThat(captor.getValue().getSeveridad()).isEqualTo(AlertaSeveridad.ALTA);
    }

    /**
     * Valida que no se generen alertas si la planta esta saludable.
     */
    @Test
    void testNoGenerarAlerta_plantaSaludable() {
        LocalDate hoy = LocalDate.of(2026, 5, 6);
        planta.setFechaUltimoRiego(hoy);
        planta.setFrecuenciaRiegoDias(3);
        planta.setFechaUltimaFertilizacion(hoy);
        planta.setFrecuenciaFertilizacionDias(10);
        planta.setEstadoActual(EstadoCultivoStatus.OPTIMO);

        when(plantaRepository.findAll()).thenReturn(List.of(planta));
        when(alertaRepository.existsByPlantaIdAndTipoAndFechaGeneracionBetween(
            eq(planta.getId()), eq(AlertaTipo.RIEGO), any(), any()))
            .thenReturn(false);
        when(alertaRepository.existsByPlantaIdAndTipoAndFechaGeneracionBetween(
            eq(planta.getId()), eq(AlertaTipo.FERTILIZACION), any(), any()))
            .thenReturn(false);
        when(alertaRepository.existsByPlantaIdAndTipoAndFechaGeneracionBetween(
            eq(planta.getId()), eq(AlertaTipo.ESTADO), any(), any()))
            .thenReturn(false);

        Map<Long, Integer> resultado = scheduler.generarAlertasAutomaticas(hoy);

        assertThat(resultado.isEmpty()).isTrue();
        verify(alertaRepository, never()).save(any(Alerta.class));
    }
}
