/*
 * Proyecto: GreenHouse Manager
 * Archivo: useAuth.js
 * Descripcion: Hook para consumir el contexto de autenticacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext.jsx";

/**
 * Returns the authentication context.
 *
 * @returns {import("../context/AuthContext.jsx").AuthContext} auth context
 */
export function useAuth() {
  return useContext(AuthContext);
}
