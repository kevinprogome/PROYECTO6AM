/*
 * Proyecto: GreenHouse Manager
 * Archivo: StatusChip.jsx
 * Descripcion: Chip para estado de cultivo.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useTranslation } from "react-i18next";

const statusStyles = {
  OPTIMO: { label: "status.optimo", className: "chip-optimo" },
  VIGILANCIA: { label: "status.vigilancia", className: "chip-vigilancia" },
  CRITICO: { label: "status.critico", className: "chip-critico" }
};

/**
 * Renders a crop status chip.
 *
 * @param {Object} props component props
 * @param {string} props.status status value
 * @returns {JSX.Element} chip component
 */
export default function StatusChip({ status }) {
  const { t } = useTranslation();
  const config = statusStyles[status] || statusStyles.VIGILANCIA;

  return <span className={`chip ${config.className}`}>{t(config.label)}</span>;
}
