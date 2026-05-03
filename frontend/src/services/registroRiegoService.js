/*
 * Proyecto: GreenHouse Manager
 * Archivo: registroRiegoService.js
 * Descripcion: Servicio API para registros de riego.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import apiClient from "./apiClient.js";

const BASE_PATH = "/api/registros-riego";

/**
 * Fetches all irrigation records.
 *
 * @returns {Promise<Array>} records
 */
export async function getAll() {
  const response = await apiClient.get(BASE_PATH);
  return response.data;
}

/**
 * Fetches an irrigation record by id.
 *
 * @param {number|string} id record id
 * @returns {Promise<Object>} record
 */
export async function getById(id) {
  const response = await apiClient.get(`${BASE_PATH}/${id}`);
  return response.data;
}

/**
 * Creates an irrigation record.
 *
 * @param {Object} payload record payload
 * @returns {Promise<Object>} created record
 */
export async function create(payload) {
  const response = await apiClient.post(BASE_PATH, payload);
  return response.data;
}

/**
 * Updates an irrigation record.
 *
 * @param {number|string} id record id
 * @param {Object} payload record payload
 * @returns {Promise<Object>} updated record
 */
export async function update(id, payload) {
  const response = await apiClient.put(`${BASE_PATH}/${id}`, payload);
  return response.data;
}

/**
 * Deletes an irrigation record.
 *
 * @param {number|string} id record id
 * @returns {Promise<void>} deletion result
 */
export async function remove(id) {
  await apiClient.delete(`${BASE_PATH}/${id}`);
}

export const registroRiegoService = { getAll, getById, create, update, delete: remove };
