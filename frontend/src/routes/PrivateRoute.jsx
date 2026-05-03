/*
 * Proyecto: GreenHouse Manager
 * Archivo: PrivateRoute.jsx
 * Descripcion: Ruta protegida por autenticacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { Navigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth.js";

/**
 * Guards routes that require authentication.
 *
 * @param {Object} props component props
 * @param {import("react").ReactNode} props.children child components
 * @returns {JSX.Element} guarded route
 */
export default function PrivateRoute({ children }) {
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
}
