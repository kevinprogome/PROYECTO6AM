/*
 * Proyecto: GreenHouse Manager
 * Archivo: Navbar.jsx
 * Descripcion: Barra de navegacion principal.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { NavLink } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useAuth } from "../hooks/useAuth.js";

/**
 * Renders the top navigation bar with language switch and logout.
 *
 * @returns {JSX.Element} Navbar component
 */
export default function Navbar() {
  const { t, i18n } = useTranslation();
  const { isAuthenticated, logout } = useAuth();

  /**
   * Handles language changes.
   *
   * @param {import("react").ChangeEvent<HTMLSelectElement>} event select event
   */
  const handleLanguageChange = (event) => {
    i18n.changeLanguage(event.target.value);
  };

  /**
   * Handles logout action.
   */
  const handleLogout = () => {
    logout();
  };

  const navItems = [
    { to: "/", label: t("nav.dashboard") },
    { to: "/invernaderos", label: t("nav.greenhouses") },
    { to: "/plantas", label: t("nav.plants") },
    { to: "/alertas", label: t("nav.alerts") },
    { to: "/reportes", label: t("nav.reports") }
  ];

  return (
    <header className="navbar">
      <div className="nav-brand">
        <span className="nav-title">{t("app.name")}</span>
        <span className="nav-subtitle">{t("app.tagline")}</span>
      </div>
      {isAuthenticated ? (
        <nav className="nav-links">
          {navItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                `nav-link${isActive ? " active" : ""}`
              }
            >
              {item.label}
            </NavLink>
          ))}
        </nav>
      ) : null}
      <div className="nav-actions">
        <select
          className="select"
          value={i18n.language}
          onChange={handleLanguageChange}
        >
          <option value="es">ES</option>
          <option value="en">EN</option>
        </select>
        {isAuthenticated ? (
          <button className="btn btn-outline" onClick={handleLogout} type="button">
            {t("nav.logout")}
          </button>
        ) : (
          <NavLink className="btn btn-primary" to="/login">
            {t("nav.login")}
          </NavLink>
        )}
      </div>
    </header>
  );
}
