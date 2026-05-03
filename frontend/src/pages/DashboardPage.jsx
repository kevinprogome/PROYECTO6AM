/*
 * Proyecto: GreenHouse Manager
 * Archivo: DashboardPage.jsx
 * Descripcion: Dashboard con metricas y graficos.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useMemo } from "react";
import { useTranslation } from "react-i18next";
import {
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis
} from "recharts";
import MetricCard from "../components/MetricCard.jsx";

/**
 * Renders the dashboard page.
 *
 * @returns {JSX.Element} dashboard page
 */
export default function DashboardPage() {
  const { t } = useTranslation();

  const metrics = useMemo(
    () => [
      { label: t("dashboard.totalPlants"), value: 128 },
      { label: t("dashboard.activeAlerts"), value: 6 },
      { label: t("dashboard.upcomingIrrigation"), value: 14 }
    ],
    [t]
  );

  const chartData = useMemo(
    () => [
      {
        name: t("dashboard.chart.w1"),
        irrigation: 18,
        fertilization: 7
      },
      {
        name: t("dashboard.chart.w2"),
        irrigation: 22,
        fertilization: 10
      },
      {
        name: t("dashboard.chart.w3"),
        irrigation: 16,
        fertilization: 6
      },
      {
        name: t("dashboard.chart.w4"),
        irrigation: 28,
        fertilization: 12
      }
    ],
    [t]
  );

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">{t("dashboard.title")}</h1>
      </div>
      <div className="grid grid-3">
        {metrics.map((metric) => (
          <MetricCard
            key={metric.label}
            label={metric.label}
            value={metric.value}
          />
        ))}
      </div>
      <div className="grid" style={{ marginTop: 24 }}>
        <div className="card chart-card">
          <h3>{t("dashboard.irrigationTrend")}</h3>
          <ResponsiveContainer width="100%" height={220}>
            <LineChart data={chartData}>
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Line type="monotone" dataKey="irrigation" stroke="#2f7a4f" />
              <Line type="monotone" dataKey="fertilization" stroke="#b46b2f" />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}
