/*
 * Proyecto: GreenHouse Manager
 * Archivo: DashboardPage.jsx
 * Descripcion: Dashboard con metricas y graficos.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useCallback, useEffect, useMemo, useState } from "react";
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
import { alertaService } from "../services/alertaService.js";
import { invernaderoService } from "../services/invernaderoService.js";
import { plantaService } from "../services/plantaService.js";
import { registroFertilizacionService } from "../services/registroFertilizacionService.js";
import { registroRiegoService } from "../services/registroRiegoService.js";

const toShortDate = (date) =>
  date.toLocaleDateString(undefined, { day: "2-digit", month: "2-digit" });

const startOfWeek = (date) => {
  const start = new Date(date);
  const day = start.getDay();
  const diff = (day === 0 ? -6 : 1) - day;
  start.setDate(start.getDate() + diff);
  start.setHours(0, 0, 0, 0);
  return start;
};

const addDays = (date, days) => {
  const next = new Date(date);
  next.setDate(next.getDate() + days);
  return next;
};

const countRecordsInRange = (records, dateKey, start, end) => {
  return records.reduce((total, record) => {
    const value = record[dateKey];
    if (!value) {
      return total;
    }
    const date = new Date(value);
    if (date >= start && date <= end) {
      return total + 1;
    }
    return total;
  }, 0);
};

/**
 * Renders the dashboard page.
 *
 * @returns {JSX.Element} dashboard page
 */
export default function DashboardPage() {
  const { t } = useTranslation();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [greenhouses, setGreenhouses] = useState([]);
  const [selectedGreenhouse, setSelectedGreenhouse] = useState("");
  const [plants, setPlants] = useState([]);
  const [alerts, setAlerts] = useState([]);
  const [needsIrrigation, setNeedsIrrigation] = useState([]);
  const [needsFertilization, setNeedsFertilization] = useState([]);
  const [irrigationRecords, setIrrigationRecords] = useState([]);
  const [fertilizationRecords, setFertilizationRecords] = useState([]);

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const [greenhouseData, plantData, alertData, riegoData, fertData, needRiego, needFert] =
        await Promise.all([
          invernaderoService.getAll(),
          plantaService.getAll(),
          alertaService.getAll(),
          registroRiegoService.getAll(),
          registroFertilizacionService.getAll(),
          plantaService.getNeedsIrrigation(),
          plantaService.getNeedsFertilization()
        ]);
      setGreenhouses(greenhouseData || []);
      setPlants(plantData || []);
      setAlerts(alertData || []);
      setIrrigationRecords(riegoData || []);
      setFertilizationRecords(fertData || []);
      setNeedsIrrigation(needRiego || []);
      setNeedsFertilization(needFert || []);
      setError("");
    } catch (loadError) {
      setError(t("common.errorLoad"));
    } finally {
      setLoading(false);
    }
  }, [t]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const selectedId = selectedGreenhouse ? Number(selectedGreenhouse) : null;

  const greenhouseMap = useMemo(() => {
    return new Map(greenhouses.map((item) => [item.id, item]));
  }, [greenhouses]);

  const filteredPlants = useMemo(() => {
    if (!selectedId) {
      return plants;
    }
    return plants.filter((plant) => plant.invernaderoId === selectedId);
  }, [plants, selectedId]);

  const activeAlerts = useMemo(() => {
    const active = alerts.filter((alert) => alert.activa);
    if (!selectedId) {
      return active;
    }
    return active.filter((alert) => alert.invernaderoId === selectedId);
  }, [alerts, selectedId]);

  const filteredNeedsIrrigation = useMemo(() => {
    if (!selectedId) {
      return needsIrrigation;
    }
    return needsIrrigation.filter((plant) => plant.invernaderoId === selectedId);
  }, [needsIrrigation, selectedId]);

  const filteredNeedsFertilization = useMemo(() => {
    if (!selectedId) {
      return needsFertilization;
    }
    return needsFertilization.filter((plant) => plant.invernaderoId === selectedId);
  }, [needsFertilization, selectedId]);

  const plantMap = useMemo(() => {
    return new Map(plants.map((plant) => [plant.id, plant]));
  }, [plants]);

  const filteredIrrigationRecords = useMemo(() => {
    if (!selectedId) {
      return irrigationRecords;
    }
    return irrigationRecords.filter((record) => {
      const plant = plantMap.get(record.plantaId);
      return plant?.invernaderoId === selectedId;
    });
  }, [irrigationRecords, plantMap, selectedId]);

  const filteredFertilizationRecords = useMemo(() => {
    if (!selectedId) {
      return fertilizationRecords;
    }
    return fertilizationRecords.filter((record) => {
      const plant = plantMap.get(record.plantaId);
      return plant?.invernaderoId === selectedId;
    });
  }, [fertilizationRecords, plantMap, selectedId]);

  const criticalPlants = useMemo(() => {
    return filteredPlants.filter((plant) => plant.estadoActual === "CRITICO");
  }, [filteredPlants]);

  const attentionPlants = useMemo(() => {
    const attentionMap = new Map();
    filteredNeedsIrrigation.forEach((plant) => {
      attentionMap.set(plant.id, {
        plant,
        needsIrrigation: true,
        needsFertilization: false
      });
    });
    filteredNeedsFertilization.forEach((plant) => {
      const existing = attentionMap.get(plant.id);
      attentionMap.set(plant.id, {
        plant,
        needsIrrigation: existing?.needsIrrigation || false,
        needsFertilization: true
      });
    });
    return Array.from(attentionMap.values()).slice(0, 6);
  }, [filteredNeedsIrrigation, filteredNeedsFertilization]);

  const metrics = useMemo(
    () => [
      { label: t("dashboard.totalPlants"), value: filteredPlants.length },
      { label: t("dashboard.activeAlerts"), value: activeAlerts.length },
      { label: t("dashboard.needsIrrigation"), value: filteredNeedsIrrigation.length },
      { label: t("dashboard.needsFertilization"), value: filteredNeedsFertilization.length },
      { label: t("dashboard.criticalPlants"), value: criticalPlants.length }
    ],
    [
      t,
      filteredPlants.length,
      activeAlerts.length,
      filteredNeedsIrrigation.length,
      filteredNeedsFertilization.length,
      criticalPlants.length
    ]
  );

  const weeklySlots = useMemo(() => {
    const currentWeekStart = startOfWeek(new Date());
    return Array.from({ length: 4 }).map((_, index) => {
      const start = addDays(currentWeekStart, (index - 3) * 7);
      const end = addDays(start, 6);
      return {
        start,
        end,
        label: `${toShortDate(start)} - ${toShortDate(end)}`
      };
    });
  }, []);

  const chartData = useMemo(() => {
    return weeklySlots.map((slot) => ({
      name: slot.label,
      irrigation: countRecordsInRange(
        filteredIrrigationRecords,
        "fechaRiego",
        slot.start,
        slot.end
      ),
      fertilization: countRecordsInRange(
        filteredFertilizationRecords,
        "fechaFertilizacion",
        slot.start,
        slot.end
      )
    }));
  }, [weeklySlots, filteredIrrigationRecords, filteredFertilizationRecords]);

  const dashboardSubtitle = selectedId
    ? greenhouseMap.get(selectedId)?.nombre || t("dashboard.activeGreenhouse")
    : t("dashboard.allGreenhouses");

  return (
    <div>
      <div className="dashboard-hero">
        <div>
          <p className="eyebrow">{t("dashboard.overview")}</p>
          <h1 className="page-title">{t("dashboard.title")}</h1>
          <p className="muted">{dashboardSubtitle}</p>
        </div>
        <div className="hero-actions">
          <select
            className="select"
            value={selectedGreenhouse}
            onChange={(event) => setSelectedGreenhouse(event.target.value)}
          >
            <option value="">{t("dashboard.allGreenhouses")}</option>
            {greenhouses.map((greenhouse) => (
              <option key={greenhouse.id} value={greenhouse.id}>
                {greenhouse.nombre} · {greenhouse.ubicacion}
              </option>
            ))}
          </select>
        </div>
      </div>

      {loading ? (
        <p className="muted">{t("common.loading")}</p>
      ) : error ? (
        <div className="notice notice-error">{error}</div>
      ) : (
        <>
          <div className="grid grid-5">
            {metrics.map((metric) => (
              <MetricCard
                key={metric.label}
                label={metric.label}
                value={metric.value}
              />
            ))}
          </div>
          <div className="grid dashboard-grid" style={{ marginTop: 24 }}>
            <div className="card chart-card">
              <div className="section-header">
                <h3>{t("dashboard.weeklyActivity")}</h3>
                <span className="muted">{t("dashboard.irrigationTrend")}</span>
              </div>
              {filteredIrrigationRecords.length === 0 && filteredFertilizationRecords.length === 0 ? (
                <p className="muted">{t("dashboard.noActivity")}</p>
              ) : (
                <ResponsiveContainer width="100%" height={220}>
                  <LineChart data={chartData}>
                    <XAxis dataKey="name" />
                    <YAxis />
                    <Tooltip />
                    <Line type="monotone" dataKey="irrigation" stroke="#2f7a4f" />
                    <Line type="monotone" dataKey="fertilization" stroke="#b46b2f" />
                  </LineChart>
                </ResponsiveContainer>
              )}
            </div>
            <div className="card attention-card">
              <div className="section-header">
                <h3>{t("dashboard.attentionList")}</h3>
                <span className="muted">{t("dashboard.attentionHint")}</span>
              </div>
              {attentionPlants.length === 0 ? (
                <p className="muted">{t("dashboard.noAttention")}</p>
              ) : (
                <ul className="stack">
                  {attentionPlants.map((item) => {
                    const greenhouse = greenhouseMap.get(item.plant.invernaderoId);
                    return (
                      <li key={item.plant.id} className="list-item">
                        <div>
                          <div className="list-title">{item.plant.nombreComun}</div>
                          <div className="muted">
                            {greenhouse ? `${greenhouse.nombre} · ${greenhouse.ubicacion}` : ""}
                          </div>
                        </div>
                        <div className="pill-group">
                          {item.needsIrrigation ? (
                            <span className="pill pill-primary">{t("dashboard.needsIrrigation")}</span>
                          ) : null}
                          {item.needsFertilization ? (
                            <span className="pill pill-warning">{t("dashboard.needsFertilization")}</span>
                          ) : null}
                        </div>
                      </li>
                    );
                  })}
                </ul>
              )}
            </div>
          </div>
        </>
      )}
    </div>
  );
}
