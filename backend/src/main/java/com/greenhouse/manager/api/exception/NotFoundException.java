/*
 * Proyecto: GreenHouse Manager
 * Archivo: NotFoundException.java
 * Descripcion: Excepcion para recursos no encontrados.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.exception;

/**
 * Runtime exception for missing resources with i18n message keys.
 */
public class NotFoundException extends RuntimeException {

    private final String messageKey;
    private final Object[] args;

    /**
     * Creates a new NotFoundException with a message key.
     *
     * @param messageKey i18n key
     */
    public NotFoundException(String messageKey) {
        this(messageKey, new Object[0]);
    }

    /**
     * Creates a new NotFoundException with a message key and arguments.
     *
     * @param messageKey i18n key
     * @param args message arguments
     */
    public NotFoundException(String messageKey, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args == null ? new Object[0] : args.clone();
    }

    /**
     * Gets the i18n message key.
     *
     * @return message key
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * Gets the message arguments.
     *
     * @return message arguments
     */
    public Object[] getArgs() {
        return args.clone();
    }
}
