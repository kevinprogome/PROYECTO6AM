/*
 * Proyecto: GreenHouse Manager
 * Archivo: AppRoutes.jsx
 * Descripcion: Definicion de rutas de la aplicacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { Navigate, Route, Routes } from "react-router-dom";
import PrivateRoute from "./PrivateRoute.jsx";
import AlertasPage from "../pages/AlertasPage.jsx";
import DashboardPage from "../pages/DashboardPage.jsx";
import InvernaderosPage from "../pages/InvernaderosPage.jsx";
import LoginPage from "../pages/LoginPage.jsx";
import PlantasPage from "../pages/PlantasPage.jsx";
import ReportesPage from "../pages/ReportesPage.jsx";

/**
 * Renders the app routes.
 *
 * @returns {JSX.Element} routes configuration
 */
export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route
        path="/"
        element={
          <PrivateRoute>
            <DashboardPage />
          </PrivateRoute>
        }
      />
      <Route
        path="/invernaderos"
        element={
          <PrivateRoute>
            <InvernaderosPage />
          </PrivateRoute>
        }
      />
      <Route
        path="/plantas"
        element={
          <PrivateRoute>
            <PlantasPage />
          </PrivateRoute>
        }
      />
      <Route
        path="/alertas"
        element={
          <PrivateRoute>
            <AlertasPage />
          </PrivateRoute>
        }
      />
      <Route
        path="/reportes"
        element={
          <PrivateRoute>
            <ReportesPage />
          </PrivateRoute>
        }
      />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
