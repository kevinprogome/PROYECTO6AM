/*
 * Proyecto: GreenHouse Manager
 * Archivo: LoginPage.jsx
 * Descripcion: Pantalla de login con OAuth2 Google.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useAuth } from "../hooks/useAuth.js";
import { getGoogleLoginUrl } from "../services/authService.js";

/**
 * Renders the login page.
 *
 * @returns {JSX.Element} login page
 */
export default function LoginPage() {
  const { t } = useTranslation();
  const { login } = useAuth();
  const [tokenInput, setTokenInput] = useState("");

  /**
   * Detects token in query params and stores it.
   */
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");
    if (token) {
      const email = params.get("email") || "user@local";
      const role = params.get("role") || "OPERADOR";
      login({ token, user: { email, role } });
    }
  }, [login]);

  /**
   * Handles redirect to Google login.
   */
  const handleGoogleLogin = () => {
    window.location.href = getGoogleLoginUrl();
  };

  /**
   * Handles manual token input changes.
   *
   * @param {import("react").ChangeEvent<HTMLInputElement>} event input event
   */
  const handleTokenChange = (event) => {
    setTokenInput(event.target.value);
  };

  /**
   * Handles manual token submission.
   *
   * @param {import("react").FormEvent<HTMLFormElement>} event form event
   */
  const handleTokenSubmit = (event) => {
    event.preventDefault();
    if (!tokenInput) {
      return;
    }
    login({ token: tokenInput, user: { email: "manual@local", role: "OPERADOR" } });
    setTokenInput("");
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
      <hr />
      <p className="muted">{t("login.tokenHint")}</p>
      <form onSubmit={handleTokenSubmit}>
        <input
          className="input"
          name="token"
          value={tokenInput}
          onChange={handleTokenChange}
          placeholder={t("login.tokenPlaceholder")}
        />
        <div className="modal-actions">
          <button className="btn btn-outline" type="submit">
            {t("login.tokenButton")}
          </button>
        </div>
      </form>
    </div>
  );
}
