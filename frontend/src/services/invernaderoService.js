/*
 * Proyecto: GreenHouse Manager
 * Archivo: invernaderoService.js
 * Descripcion: Servicio API para invernaderos.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import apiClient from "./apiClient.js";

const BASE_PATH = "/api/invernaderos";

/**
 * Fetches all greenhouses.
 *
 * @returns {Promise<Array>} greenhouses
 */
export async function getAll() {
  const response = await apiClient.get(BASE_PATH);
  return response.data;
}

/**
 * Fetches a greenhouse by id.
 *
 * @param {number|string} id greenhouse id
 * @returns {Promise<Object>} greenhouse
 */
export async function getById(id) {
  const response = await apiClient.get(`${BASE_PATH}/${id}`);
  return response.data;
}

/**
 * Creates a greenhouse.
 *
 * @param {Object} payload greenhouse payload
 * @returns {Promise<Object>} created greenhouse
 */
export async function create(payload) {
  const response = await apiClient.post(BASE_PATH, payload);
  return response.data;
}

/**
 * Updates a greenhouse.
 *
 * @param {number|string} id greenhouse id
 * @param {Object} payload greenhouse payload
 * @returns {Promise<Object>} updated greenhouse
 */
export async function update(id, payload) {
  const response = await apiClient.put(`${BASE_PATH}/${id}`, payload);
  return response.data;
}

/**
 * Deletes a greenhouse.
 *
 * @param {number|string} id greenhouse id
 * @returns {Promise<void>} deletion result
 */
export async function remove(id) {
  await apiClient.delete(`${BASE_PATH}/${id}`);
}

export const invernaderoService = { getAll, getById, create, update, delete: remove };
