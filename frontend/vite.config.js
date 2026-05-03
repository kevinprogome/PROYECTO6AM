/*
 * Proyecto: GreenHouse Manager
 * Archivo: vite.config.js
 * Descripcion: Configuracion de Vite para React.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

/**
 * Vite configuration for the frontend.
 */
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173
  }
});
