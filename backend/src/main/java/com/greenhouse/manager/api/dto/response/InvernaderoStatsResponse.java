/*
 * Proyecto: GreenHouse Manager
 * Archivo: InvernaderoStatsResponse.java
 * Descripcion: DTO de respuesta para estadisticas del invernadero.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.dto.response;

/**
 * Response DTO for greenhouse statistics.
 */
public class InvernaderoStatsResponse {

    private Long invernaderoId;
    private Integer totalPlantas;
    private Integer alertasActivas;
    private Integer plantasNecesitanRiego;
    private Integer plantasNecesitanFertilizacion;

    /**
     * Gets the greenhouse id.
     *
     * @return greenhouse id
     */
    public Long getInvernaderoId() {
        return invernaderoId;
    }

    /**
     * Sets the greenhouse id.
     *
     * @param invernaderoId greenhouse id
     */
    public void setInvernaderoId(Long invernaderoId) {
        this.invernaderoId = invernaderoId;
    }

    /**
     * Gets the total plants count.
     *
     * @return total plants
     */
    public Integer getTotalPlantas() {
        return totalPlantas;
    }

    /**
     * Sets the total plants count.
     *
     * @param totalPlantas total plants
     */
    public void setTotalPlantas(Integer totalPlantas) {
        this.totalPlantas = totalPlantas;
    }

    /**
     * Gets the active alerts count.
     *
     * @return active alerts
     */
    public Integer getAlertasActivas() {
        return alertasActivas;
    }

    /**
     * Sets the active alerts count.
     *
     * @param alertasActivas active alerts
     */
    public void setAlertasActivas(Integer alertasActivas) {
        this.alertasActivas = alertasActivas;
    }

    /**
     * Gets the plants needing irrigation count.
     *
     * @return plants needing irrigation
     */
    public Integer getPlantasNecesitanRiego() {
        return plantasNecesitanRiego;
    }

    /**
     * Sets the plants needing irrigation count.
     *
     * @param plantasNecesitanRiego plants needing irrigation
     */
    public void setPlantasNecesitanRiego(Integer plantasNecesitanRiego) {
        this.plantasNecesitanRiego = plantasNecesitanRiego;
    }

    /**
     * Gets the plants needing fertilization count.
     *
     * @return plants needing fertilization
     */
    public Integer getPlantasNecesitanFertilizacion() {
        return plantasNecesitanFertilizacion;
    }

    /**
     * Sets the plants needing fertilization count.
     *
     * @param plantasNecesitanFertilizacion plants needing fertilization
     */
    public void setPlantasNecesitanFertilizacion(Integer plantasNecesitanFertilizacion) {
        this.plantasNecesitanFertilizacion = plantasNecesitanFertilizacion;
    }
}
