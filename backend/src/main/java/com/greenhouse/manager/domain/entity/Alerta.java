/*
 * Proyecto: GreenHouse Manager
 * Archivo: Alerta.java
 * Descripcion: Entidad para alertas del sistema.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.domain.entity;

import com.greenhouse.manager.domain.enums.AlertaSeveridad;
import com.greenhouse.manager.domain.enums.AlertaTipo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Entity representing system alerts.
 */
@Entity
@Table(name = "alertas")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "planta_id", nullable = false)
    private Planta planta;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invernadero_id", nullable = false)
    private Invernadero invernadero;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AlertaTipo tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private AlertaSeveridad severidad;

    @NotBlank
    @Size(max = 300)
    @Column(length = 300, nullable = false)
    private String mensaje;

    @NotNull
    @Column(nullable = false)
    private Boolean activa;

    @NotNull
    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resuelta_por_usuario_id")
    private Usuario resueltaPorUsuario;

    /**
     * Default constructor required by JPA.
     */
    public Alerta() {
        // Default constructor
    }

    /**
     * Initializes fields before persistence.
     */
    @PrePersist
    public void prePersist() {
        if (fechaGeneracion == null) {
            fechaGeneracion = LocalDateTime.now();
        }
    }

    /**
     * Gets the unique identifier.
     *
     * @return alert id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier.
     *
     * @param id alert id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the plant linked to the alert.
     *
     * @return plant
     */
    public Planta getPlanta() {
        return planta;
    }

    /**
     * Sets the plant linked to the alert.
     *
     * @param planta plant
     */
    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    /**
     * Gets the greenhouse linked to the alert.
     *
     * @return greenhouse
     */
    public Invernadero getInvernadero() {
        return invernadero;
    }

    /**
     * Sets the greenhouse linked to the alert.
     *
     * @param invernadero greenhouse
     */
    public void setInvernadero(Invernadero invernadero) {
        this.invernadero = invernadero;
    }

    /**
     * Gets the alert type.
     *
     * @return alert type
     */
    public AlertaTipo getTipo() {
        return tipo;
    }

    /**
     * Sets the alert type.
     *
     * @param tipo alert type
     */
    public void setTipo(AlertaTipo tipo) {
        this.tipo = tipo;
    }

    /**
     * Gets the alert severity.
     *
     * @return severity
     */
    public AlertaSeveridad getSeveridad() {
        return severidad;
    }

    /**
     * Sets the alert severity.
     *
     * @param severidad severity
     */
    public void setSeveridad(AlertaSeveridad severidad) {
        this.severidad = severidad;
    }

    /**
     * Gets the message.
     *
     * @return message
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Sets the message.
     *
     * @param mensaje message
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Gets whether the alert is active.
     *
     * @return active flag
     */
    public Boolean getActiva() {
        return activa;
    }

    /**
     * Sets whether the alert is active.
     *
     * @param activa active flag
     */
    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    /**
     * Gets the generation timestamp.
     *
     * @return generation timestamp
     */
    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    /**
     * Sets the generation timestamp.
     *
     * @param fechaGeneracion generation timestamp
     */
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    /**
     * Gets the resolution timestamp.
     *
     * @return resolution timestamp
     */
    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    /**
     * Sets the resolution timestamp.
     *
     * @param fechaResolucion resolution timestamp
     */
    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    /**
     * Gets the resolving user.
     *
     * @return resolving user
     */
    public Usuario getResueltaPorUsuario() {
        return resueltaPorUsuario;
    }

    /**
     * Sets the resolving user.
     *
     * @param resueltaPorUsuario resolving user
     */
    public void setResueltaPorUsuario(Usuario resueltaPorUsuario) {
        this.resueltaPorUsuario = resueltaPorUsuario;
    }
}
