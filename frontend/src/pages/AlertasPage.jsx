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
import { useAuth } from "../hooks/useAuth.js";
import { alertaService } from "../services/alertaService.js";

/**
 * Formats a date into a local datetime string.
 *
 * @param {Date} date date instance
 * @returns {string} local datetime
 */
const toLocalDateTime = (date) => {
  const offsetMs = date.getTimezoneOffset() * 60000;
  return new Date(date.getTime() - offsetMs).toISOString().slice(0, 19);
};

/**
 * Renders the alerts page.
 *
 * @returns {JSX.Element} page component
 */
export default function AlertasPage() {
  const { t } = useTranslation();
  const { user } = useAuth();
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [saveError, setSaveError] = useState("");
  const [statusFilter, setStatusFilter] = useState("");
  const [severityFilter, setSeverityFilter] = useState("");

  const formatDateTime = (value) =>
    value ? new Date(value).toLocaleString() : t("common.notAvailable");

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
    setSaveError("");
    try {
      await alertaService.update(row.id, {
        plantaId: row.plantaId,
        invernaderoId: row.invernaderoId,
        tipo: row.tipo,
        severidad: row.severidad,
        mensaje: row.mensaje,
        activa: false,
        fechaGeneracion: row.fechaGeneracion,
        fechaResolucion: toLocalDateTime(new Date()),
        resueltaPorUsuarioId: user?.id || row.resueltaPorUsuarioId || null
      });
      fetchData();
    } catch (saveErrorResponse) {
      const apiMessage = saveErrorResponse?.response?.data?.message;
      setSaveError(apiMessage || t("common.saveError"));
    }
  };

  /**
   * Renders row actions for the table.
   *
   * @param {Object} row alert row
   * @returns {JSX.Element} actions
   */
  const renderActions = (row) => (
    <div className="table-actions">
      <button
        className="btn btn-outline"
        type="button"
        onClick={() => handleResolve(row)}
        disabled={!row.activa}
      >
        {t("alerts.resolve")}
      </button>
    </div>
  );

  const filteredItems = useMemo(() => {
    return items.filter((item) => {
      const statusOk = statusFilter === ""
        ? true
        : statusFilter === "active"
          ? item.activa
          : !item.activa;
      const severityOk = severityFilter ? item.severidad === severityFilter : true;
      return statusOk && severityOk;
    });
  }, [items, statusFilter, severityFilter]);

  const columns = useMemo(
    () => [
      {
        key: "tipo",
        header: t("alerts.table.type"),
        render: (row) => t(`alerts.types.${row.tipo}`, { defaultValue: row.tipo })
      },
      {
        key: "severidad",
        header: t("alerts.table.severity"),
        render: (row) => <AlertBadge severity={row.severidad} />
      },
      {
        key: "mensaje",
        header: t("alerts.table.message"),
        render: (row) => t(row.mensaje, { defaultValue: row.mensaje })
      },
      {
        key: "activa",
        header: t("alerts.table.status"),
        render: (row) => (row.activa ? t("common.active") : t("common.inactive"))
      },
      {
        key: "fechaGeneracion",
        header: t("alerts.table.created"),
        render: (row) => formatDateTime(row.fechaGeneracion)
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
      <div className="filters">
        <select
          className="select"
          value={statusFilter}
          onChange={(event) => setStatusFilter(event.target.value)}
        >
          <option value="">{t("alerts.filterStatus")}</option>
          <option value="active">{t("common.active")}</option>
          <option value="inactive">{t("common.inactive")}</option>
        </select>
        <select
          className="select"
          value={severityFilter}
          onChange={(event) => setSeverityFilter(event.target.value)}
        >
          <option value="">{t("alerts.filterSeverity")}</option>
          <option value="BAJA">{t("severity.baja")}</option>
          <option value="MEDIA">{t("severity.media")}</option>
          <option value="ALTA">{t("severity.alta")}</option>
        </select>
      </div>
      {saveError ? <div className="notice notice-error">{saveError}</div> : null}
      {loading ? (
        <p className="muted">{t("common.loading")}</p>
      ) : error ? (
        <p className="muted">{error}</p>
      ) : (
        <DataTable columns={columns} rows={filteredItems} emptyMessage={t("alerts.empty")} />
      )}
    </div>
  );
}
