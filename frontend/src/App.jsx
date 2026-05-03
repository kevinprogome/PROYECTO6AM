/*
 * Proyecto: GreenHouse Manager
 * Archivo: App.jsx
 * Descripcion: Componente raiz de la aplicacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { BrowserRouter } from "react-router-dom";
import Navbar from "./components/Navbar.jsx";
import AppRoutes from "./routes/AppRoutes.jsx";

/**
 * Renders the main application shell with navigation and routes.
 *
 * @returns {JSX.Element} App component
 */
export default function App() {
  return (
    <BrowserRouter>
      <div className="app-shell">
        <Navbar />
        <main className="app-content">
          <AppRoutes />
        </main>
      </div>
    </BrowserRouter>
  );
}
