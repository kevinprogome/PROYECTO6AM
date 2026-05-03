/*
 * Proyecto: GreenHouse Manager
 * Archivo: main.jsx
 * Descripcion: Punto de entrada de la aplicacion React.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.jsx";
import "./i18n/index.js";
import "./index.css";
import { AuthProvider } from "./context/AuthContext.jsx";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <AuthProvider>
      <App />
    </AuthProvider>
  </React.StrictMode>
);
