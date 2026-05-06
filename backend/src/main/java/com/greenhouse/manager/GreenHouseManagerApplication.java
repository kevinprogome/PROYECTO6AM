/*
 * Proyecto: GreenHouse Manager
 * Archivo: GreenHouseManagerApplication.java
 * Descripcion: Clase principal para iniciar la aplicacion Spring Boot.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point for the GreenHouse Manager backend application.
 */
@SpringBootApplication
@EnableScheduling
public class GreenHouseManagerApplication {

    /**
     * Application main method.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(GreenHouseManagerApplication.class, args);
    }
}
