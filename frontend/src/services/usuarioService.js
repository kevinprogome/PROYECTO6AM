/*
 * Proyecto: GreenHouse Manager
 * Archivo: usuarioService.js
 * Descripcion: Servicio API para usuarios.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import apiClient from "./apiClient.js";

const BASE_PATH = "/api/usuarios";

/**
 * Fetches all users.
 *
 * @returns {Promise<Array>} users
 */
export async function getAll() {
  const response = await apiClient.get(BASE_PATH);
  return response.data;
}

/**
 * Fetches a user by id.
 *
 * @param {number|string} id user id
 * @returns {Promise<Object>} user
 */
export async function getById(id) {
  const response = await apiClient.get(`${BASE_PATH}/${id}`);
  return response.data;
}

/**
 * Creates a user.
 *
 * @param {Object} payload user payload
 * @returns {Promise<Object>} created user
 */
export async function create(payload) {
  const response = await apiClient.post(BASE_PATH, payload);
  return response.data;
}

/**
 * Updates a user.
 *
 * @param {number|string} id user id
 * @param {Object} payload user payload
 * @returns {Promise<Object>} updated user
 */
export async function update(id, payload) {
  const response = await apiClient.put(`${BASE_PATH}/${id}`, payload);
  return response.data;
}

/**
 * Deletes a user.
 *
 * @param {number|string} id user id
 * @returns {Promise<void>} deletion result
 */
export async function remove(id) {
  await apiClient.delete(`${BASE_PATH}/${id}`);
}

export const usuarioService = { getAll, getById, create, update, delete: remove };
