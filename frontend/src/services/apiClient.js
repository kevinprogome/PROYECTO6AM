/*
 * Proyecto: GreenHouse Manager
 * Archivo: apiClient.js
 * Descripcion: Cliente Axios con interceptor JWT.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import axios from "axios";

/**
 * Base URL for backend API.
 */
export const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

/**
 * Reads the JWT token from storage.
 *
 * @returns {string|null} jwt token
 */
function getToken() {
  return localStorage.getItem("ghm_token");
}

const apiClient = axios.create({
  baseURL: API_BASE_URL
});

apiClient.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default apiClient;
