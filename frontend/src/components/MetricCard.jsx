/*
 * Proyecto: GreenHouse Manager
 * Archivo: MetricCard.jsx
 * Descripcion: Tarjeta para metricas del dashboard.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */

/**
 * Renders a metric card.
 *
 * @param {Object} props component props
 * @param {string} props.label metric label
 * @param {string|number} props.value metric value
 * @param {string} [props.hint] helper hint
 * @returns {JSX.Element} metric card
 */
export default function MetricCard({ label, value, hint }) {
  return (
    <div className="card">
      <div className="muted">{label}</div>
      <div className="metric-value">{value}</div>
      {hint ? <div className="metric-hint">{hint}</div> : null}
    </div>
  );
}
