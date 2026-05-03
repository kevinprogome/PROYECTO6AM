/*
 * Proyecto: GreenHouse Manager
 * Archivo: index.js
 * Descripcion: Inicializacion de i18next para la aplicacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import es from "./es.json";
import en from "./en.json";

/**
 * Initializes i18next with Spanish default and English fallback.
 */
i18n.use(initReactI18next).init({
  resources: {
    es: { translation: es },
    en: { translation: en }
  },
  lng: "es",
  fallbackLng: "en",
  interpolation: {
    escapeValue: false
  }
});

export default i18n;
