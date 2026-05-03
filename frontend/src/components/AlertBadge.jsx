/*
 * Proyecto: GreenHouse Manager
 * Archivo: AlertBadge.jsx
 * Descripcion: Badge para severidad de alertas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useTranslation } from "react-i18next";

const severityStyles = {
  BAJA: { label: "severity.baja", className: "badge-low" },
  MEDIA: { label: "severity.media", className: "badge-medium" },
  ALTA: { label: "severity.alta", className: "badge-high" }
};

/**
 * Renders an alert severity badge.
 *
 * @param {Object} props component props
 * @param {string} props.severity severity value
 * @returns {JSX.Element} badge component
 */
export default function AlertBadge({ severity }) {
  const { t } = useTranslation();
  const config = severityStyles[severity] || severityStyles.MEDIA;

  return (
    <span className={`badge ${config.className}`}>{t(config.label)}</span>
  );
}
