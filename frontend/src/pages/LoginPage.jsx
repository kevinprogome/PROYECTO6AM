/*
 * Proyecto: GreenHouse Manager
 * Archivo: LoginPage.jsx
 * Descripcion: Pantalla de login con OAuth2 Google.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth.js";
import { getGoogleLoginUrl } from "../services/authService.js";

/**
 * Renders the login page.
 *
 * @returns {JSX.Element} login page
 */
export default function LoginPage() {
  const { t } = useTranslation();
  const { login, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  /**
   * Detects token in query params and stores it.
   */
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");
    if (token) {
      const userId = params.get("userId");
      const email = params.get("email") || "user@local";
      const role = params.get("role") || "OPERADOR";
      login({ token, user: { id: userId ? Number(userId) : null, email, role } });
      navigate("/", { replace: true });
    }
  }, [login, navigate]);

  useEffect(() => {
    if (isAuthenticated) {
      navigate("/", { replace: true });
    }
  }, [isAuthenticated, navigate]);

  /**
   * Handles redirect to Google login.
   */
  const handleGoogleLogin = () => {
    window.location.href = getGoogleLoginUrl();
  };


  return (
    <div className="card">
      <div className="page-header">
        <h1 className="page-title">{t("login.title")}</h1>
      </div>
      <p className="muted">{t("login.subtitle")}</p>
      <button className="btn btn-primary" type="button" onClick={handleGoogleLogin}>
        {t("login.googleButton")}
      </button>
    </div>
  );
}
