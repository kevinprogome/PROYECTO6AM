/*
 * Proyecto: GreenHouse Manager
 * Archivo: AuthContext.jsx
 * Descripcion: Contexto de autenticacion para la aplicacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { createContext, useCallback, useEffect, useMemo, useState } from "react";

const TOKEN_KEY = "ghm_token";
const USER_KEY = "ghm_user";

/**
 * @typedef {Object} AuthUser
 * @property {string} email User email
 * @property {string} role User role
 */

/**
 * @typedef {Object} AuthPayload
 * @property {string} token JWT token
 * @property {AuthUser} user Authenticated user
 */

/**
 * Authentication context with user session details.
 */
export const AuthContext = createContext({
  user: null,
  token: null,
  isAuthenticated: false,
  login: () => {},
  logout: () => {}
});

/**
 * Provides authentication state to the application.
 *
 * @param {Object} props component props
 * @param {import("react").ReactNode} props.children child components
 * @returns {JSX.Element} provider component
 */
export function AuthProvider({ children }) {
  const [token, setToken] = useState(null);
  const [user, setUser] = useState(null);

  /**
   * Loads stored authentication data from local storage.
   */
  const loadStoredAuth = useCallback(() => {
    const storedToken = localStorage.getItem(TOKEN_KEY);
    const storedUser = localStorage.getItem(USER_KEY);
    if (storedToken) {
      setToken(storedToken);
    }
    if (storedUser) {
      try {
        setUser(JSON.parse(storedUser));
      } catch (error) {
        setUser(null);
      }
    }
  }, []);

  useEffect(() => {
    loadStoredAuth();
  }, [loadStoredAuth]);

  /**
   * Stores authentication data in memory and local storage.
   *
   * @param {AuthPayload} payload auth payload
   */
  const login = useCallback((payload) => {
    if (!payload || !payload.token) {
      return;
    }
    setToken(payload.token);
    setUser(payload.user || null);
    localStorage.setItem(TOKEN_KEY, payload.token);
    localStorage.setItem(USER_KEY, JSON.stringify(payload.user || null));
  }, []);

  /**
   * Clears authentication data.
   */
  const logout = useCallback(() => {
    setToken(null);
    setUser(null);
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  }, []);

  const value = useMemo(
    () => ({
      user,
      token,
      isAuthenticated: Boolean(token),
      login,
      logout
    }),
    [user, token, login, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
