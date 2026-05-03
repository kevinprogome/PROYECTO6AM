/*
 * Proyecto: GreenHouse Manager
 * Archivo: alertaService.js
 * Descripcion: Servicio API para alertas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import apiClient from "./apiClient.js";

const BASE_PATH = "/api/alertas";

/**
 * Fetches all alerts.
 *
 * @returns {Promise<Array>} alerts
 */
export async function getAll() {
  const response = await apiClient.get(BASE_PATH);
  return response.data;
}

/**
 * Fetches an alert by id.
 *
 * @param {number|string} id alert id
 * @returns {Promise<Object>} alert
 */
export async function getById(id) {
  const response = await apiClient.get(`${BASE_PATH}/${id}`);
  return response.data;
}

/**
 * Creates an alert.
 *
 * @param {Object} payload alert payload
 * @returns {Promise<Object>} created alert
 */
export async function create(payload) {
  const response = await apiClient.post(BASE_PATH, payload);
  return response.data;
}

/**
 * Updates an alert.
 *
 * @param {number|string} id alert id
 * @param {Object} payload alert payload
 * @returns {Promise<Object>} updated alert
 */
export async function update(id, payload) {
  const response = await apiClient.put(`${BASE_PATH}/${id}`, payload);
  return response.data;
}

/**
 * Deletes an alert.
 *
 * @param {number|string} id alert id
 * @returns {Promise<void>} deletion result
 */
export async function remove(id) {
  await apiClient.delete(`${BASE_PATH}/${id}`);
}

export const alertaService = { getAll, getById, create, update, delete: remove };
