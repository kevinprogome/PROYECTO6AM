/*
 * Proyecto: GreenHouse Manager
 * Archivo: plantaService.js
 * Descripcion: Servicio API para plantas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import apiClient from "./apiClient.js";

const BASE_PATH = "/api/plantas";

/**
 * Fetches all plants.
 *
 * @returns {Promise<Array>} plants
 */
export async function getAll() {
  const response = await apiClient.get(BASE_PATH);
  return response.data;
}

/**
 * Fetches a plant by id.
 *
 * @param {number|string} id plant id
 * @returns {Promise<Object>} plant
 */
export async function getById(id) {
  const response = await apiClient.get(`${BASE_PATH}/${id}`);
  return response.data;
}

/**
 * Creates a plant.
 *
 * @param {Object} payload plant payload
 * @returns {Promise<Object>} created plant
 */
export async function create(payload) {
  const response = await apiClient.post(BASE_PATH, payload);
  return response.data;
}

/**
 * Updates a plant.
 *
 * @param {number|string} id plant id
 * @param {Object} payload plant payload
 * @returns {Promise<Object>} updated plant
 */
export async function update(id, payload) {
  const response = await apiClient.put(`${BASE_PATH}/${id}`, payload);
  return response.data;
}

/**
 * Deletes a plant.
 *
 * @param {number|string} id plant id
 * @returns {Promise<void>} deletion result
 */
export async function remove(id) {
  await apiClient.delete(`${BASE_PATH}/${id}`);
}

export const plantaService = { getAll, getById, create, update, delete: remove };
