/*
 * Proyecto: GreenHouse Manager
 * Archivo: authService.js
 * Descripcion: Utilidades para autenticacion OAuth2.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { API_BASE_URL } from "./apiClient.js";

const DEFAULT_AUTH_URL = `${API_BASE_URL}/oauth2/authorization/google`;

/**
 * Returns the Google login URL.
 *
 * @returns {string} login URL
 */
export function getGoogleLoginUrl() {
  return import.meta.env.VITE_AUTH_URL || DEFAULT_AUTH_URL;
}
