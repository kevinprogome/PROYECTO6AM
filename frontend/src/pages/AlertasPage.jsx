/*
 * Proyecto: GreenHouse Manager
 * Archivo: AlertasPage.jsx
 * Descripcion: Pagina de alertas activas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useCallback, useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import AlertBadge from "../components/AlertBadge.jsx";
import DataTable from "../components/DataTable.jsx";
import { alertaService } from "../services/alertaService.js";

/**
 * Formats a date into a local datetime string.
 *
 * @param {Date} date date instance
 * @returns {string} local datetime
 */
const toLocalDateTime = (date) => date.toISOString().slice(0, 19);

/**
 * Renders the alerts page.
 *
 * @returns {JSX.Element} page component
 */
export default function AlertasPage() {
  const { t } = useTranslation();
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  /**
   * Loads alerts data.
   */
  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const data = await alertaService.getAll();
      setItems(data || []);
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

  /**
   * Marks an alert as resolved.
   *
   * @param {Object} row alert row
   */
  const handleResolve = async (row) => {
    await alertaService.update(row.id, {
      plantaId: row.plantaId,
      invernaderoId: row.invernaderoId,
      tipo: row.tipo,
      severidad: row.severidad,
      mensaje: row.mensaje,
      activa: false,
      fechaGeneracion: row.fechaGeneracion,
      fechaResolucion: toLocalDateTime(new Date()),
      resueltaPorUsuarioId: row.resueltaPorUsuarioId || null
    });
    fetchData();
  };

  /**
   * Renders row actions for the table.
   *
   * @param {Object} row alert row
   * @returns {JSX.Element} actions
   */
  const renderActions = (row) => (
    <div className="table-actions">
      <button className="btn btn-outline" type="button" onClick={() => handleResolve(row)}>
        {t("alerts.resolve")}
      </button>
    </div>
  );

  const columns = useMemo(
    () => [
      { key: "tipo", header: t("alerts.table.type") },
      {
        key: "severidad",
        header: t("alerts.table.severity"),
        render: (row) => <AlertBadge severity={row.severidad} />
      },
      { key: "mensaje", header: t("alerts.table.message") },
      {
        key: "activa",
        header: t("alerts.table.status"),
        render: (row) => (row.activa ? t("common.active") : t("common.inactive"))
      },
      { key: "actions", header: t("alerts.table.actions"), render: renderActions }
    ],
    [t]
  );

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">{t("alerts.title")}</h1>
      </div>
      {loading ? (
        <p className="muted">{t("common.loading")}</p>
      ) : error ? (
        <p className="muted">{error}</p>
      ) : (
        <DataTable columns={columns} rows={items} />
      )}
    </div>
  );
}
