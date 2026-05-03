/*
 * Proyecto: GreenHouse Manager
 * Archivo: PlantasPage.jsx
 * Descripcion: Pagina CRUD de plantas y registros rapidos.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useCallback, useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import DataTable from "../components/DataTable.jsx";
import Modal from "../components/Modal.jsx";
import StatusChip from "../components/StatusChip.jsx";
import { plantaService } from "../services/plantaService.js";
import { registroFertilizacionService } from "../services/registroFertilizacionService.js";
import { registroRiegoService } from "../services/registroRiegoService.js";

const emptyForm = {
  invernaderoId: "",
  nombreComun: "",
  nombreCientifico: "",
  variedad: "",
  fechaSiembra: "",
  fechaUltimoRiego: "",
  frecuenciaRiegoDias: "",
  fechaUltimaFertilizacion: "",
  frecuenciaFertilizacionDias: "",
  estadoActual: "OPTIMO",
  observaciones: "",
  activo: "true"
};

/**
 * Formats a date into a local datetime string.
 *
 * @param {Date} date date instance
 * @returns {string} local datetime
 */
const toLocalDateTime = (date) => date.toISOString().slice(0, 19);

/**
 * Renders the plants CRUD page.
 *
 * @returns {JSX.Element} page component
 */
export default function PlantasPage() {
  const { t } = useTranslation();
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [modalOpen, setModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState(emptyForm);
  const [estadoFilter, setEstadoFilter] = useState("");
  const [invernaderoFilter, setInvernaderoFilter] = useState("");

  /**
   * Loads plants data.
   */
  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const data = await plantaService.getAll();
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
   * Opens the modal for creating a plant.
   */
  const handleCreate = () => {
    setEditingId(null);
    setForm(emptyForm);
    setModalOpen(true);
  };

  /**
   * Opens the modal for editing a plant.
   *
   * @param {Object} row plant row
   */
  const handleEdit = (row) => {
    setEditingId(row.id);
    setForm({
      invernaderoId: row.invernaderoId || "",
      nombreComun: row.nombreComun || "",
      nombreCientifico: row.nombreCientifico || "",
      variedad: row.variedad || "",
      fechaSiembra: row.fechaSiembra || "",
      fechaUltimoRiego: row.fechaUltimoRiego || "",
      frecuenciaRiegoDias: row.frecuenciaRiegoDias || "",
      fechaUltimaFertilizacion: row.fechaUltimaFertilizacion || "",
      frecuenciaFertilizacionDias: row.frecuenciaFertilizacionDias || "",
      estadoActual: row.estadoActual || "OPTIMO",
      observaciones: row.observaciones || "",
      activo: row.activo ? "true" : "false"
    });
    setModalOpen(true);
  };

  /**
   * Handles input changes in the form.
   *
   * @param {import("react").ChangeEvent<HTMLInputElement|HTMLSelectElement>} event input event
   */
  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  };

  /**
   * Saves the plant changes.
   */
  const handleSave = async () => {
    const payload = {
      invernaderoId: Number(form.invernaderoId),
      nombreComun: form.nombreComun,
      nombreCientifico: form.nombreCientifico || null,
      variedad: form.variedad || null,
      fechaSiembra: form.fechaSiembra || null,
      fechaUltimoRiego: form.fechaUltimoRiego || null,
      frecuenciaRiegoDias: Number(form.frecuenciaRiegoDias),
      fechaUltimaFertilizacion: form.fechaUltimaFertilizacion || null,
      frecuenciaFertilizacionDias: Number(form.frecuenciaFertilizacionDias),
      estadoActual: form.estadoActual,
      observaciones: form.observaciones || null,
      activo: form.activo === "true"
    };

    if (editingId) {
      await plantaService.update(editingId, payload);
    } else {
      await plantaService.create(payload);
    }

    setModalOpen(false);
    fetchData();
  };

  /**
   * Deletes a plant.
   *
   * @param {Object} row plant row
   */
  const handleDelete = async (row) => {
    if (!window.confirm(t("common.confirmDelete"))) {
      return;
    }
    await plantaService.delete(row.id);
    fetchData();
  };

  /**
   * Registers an irrigation record for a plant.
   *
   * @param {Object} row plant row
   */
  const handleRegistrarRiego = async (row) => {
    await registroRiegoService.create({
      plantaId: row.id,
      fechaRiego: toLocalDateTime(new Date())
    });
    fetchData();
  };

  /**
   * Registers a fertilization record for a plant.
   *
   * @param {Object} row plant row
   */
  const handleRegistrarFertilizacion = async (row) => {
    const tipo = window.prompt(t("plants.promptFertilizerType"));
    if (!tipo) {
      return;
    }
    await registroFertilizacionService.create({
      plantaId: row.id,
      fechaFertilizacion: toLocalDateTime(new Date()),
      tipoFertilizante: tipo
    });
    fetchData();
  };

  /**
   * Filters plants by status and greenhouse id.
   *
   * @returns {Object[]} filtered rows
   */
  const filteredItems = useMemo(() => {
    return items.filter((item) => {
      const statusOk = estadoFilter ? item.estadoActual === estadoFilter : true;
      const greenhouseOk = invernaderoFilter
        ? String(item.invernaderoId) === invernaderoFilter
        : true;
      return statusOk && greenhouseOk;
    });
  }, [items, estadoFilter, invernaderoFilter]);

  /**
   * Renders row actions for the table.
   *
   * @param {Object} row plant row
   * @returns {JSX.Element} actions
   */
  const renderActions = (row) => (
    <div className="table-actions">
      <button className="btn btn-outline" type="button" onClick={() => handleEdit(row)}>
        {t("common.edit")}
      </button>
      <button className="btn btn-danger" type="button" onClick={() => handleDelete(row)}>
        {t("common.delete")}
      </button>
      <button className="btn btn-outline" type="button" onClick={() => handleRegistrarRiego(row)}>
        {t("plants.registerIrrigation")}
      </button>
      <button className="btn btn-outline" type="button" onClick={() => handleRegistrarFertilizacion(row)}>
        {t("plants.registerFertilization")}
      </button>
    </div>
  );

  const columns = useMemo(
    () => [
      { key: "nombreComun", header: t("plants.table.commonName") },
      {
        key: "estadoActual",
        header: t("plants.table.status"),
        render: (row) => <StatusChip status={row.estadoActual} />
      },
      { key: "invernaderoId", header: t("plants.table.greenhouse") },
      {
        key: "fechaUltimoRiego",
        header: t("plants.table.lastIrrigation"),
        render: (row) => row.fechaUltimoRiego || t("common.notAvailable")
      },
      {
        key: "fechaUltimaFertilizacion",
        header: t("plants.table.lastFertilization"),
        render: (row) => row.fechaUltimaFertilizacion || t("common.notAvailable")
      },
      { key: "actions", header: t("plants.table.actions"), render: renderActions }
    ],
    [t]
  );

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">{t("plants.title")}</h1>
        <button className="btn btn-primary" type="button" onClick={handleCreate}>
          {t("plants.create")}
        </button>
      </div>
      <div className="filters">
        <select
          className="select"
          value={estadoFilter}
          onChange={(event) => setEstadoFilter(event.target.value)}
        >
          <option value="">{t("plants.filterStatus")}</option>
          <option value="OPTIMO">{t("status.optimo")}</option>
          <option value="VIGILANCIA">{t("status.vigilancia")}</option>
          <option value="CRITICO">{t("status.critico")}</option>
        </select>
        <input
          className="input"
          value={invernaderoFilter}
          onChange={(event) => setInvernaderoFilter(event.target.value)}
          placeholder={t("plants.filterGreenhouse")}
        />
      </div>
      {loading ? (
        <p className="muted">{t("common.loading")}</p>
      ) : error ? (
        <p className="muted">{error}</p>
      ) : (
        <DataTable columns={columns} rows={filteredItems} />
      )}
      <Modal
        isOpen={modalOpen}
        title={editingId ? t("plants.edit") : t("plants.create")}
        onClose={() => setModalOpen(false)}
        onSubmit={handleSave}
      >
        <div className="form-grid">
          <input
            className="input"
            name="invernaderoId"
            value={form.invernaderoId}
            onChange={handleChange}
            placeholder={t("plants.form.greenhouseId")}
          />
          <input
            className="input"
            name="nombreComun"
            value={form.nombreComun}
            onChange={handleChange}
            placeholder={t("plants.form.commonName")}
          />
          <input
            className="input"
            name="nombreCientifico"
            value={form.nombreCientifico}
            onChange={handleChange}
            placeholder={t("plants.form.scientificName")}
          />
          <input
            className="input"
            name="variedad"
            value={form.variedad}
            onChange={handleChange}
            placeholder={t("plants.form.variety")}
          />
          <input
            className="input"
            type="date"
            name="fechaSiembra"
            value={form.fechaSiembra}
            onChange={handleChange}
            placeholder={t("plants.form.sowDate")}
          />
          <input
            className="input"
            type="date"
            name="fechaUltimoRiego"
            value={form.fechaUltimoRiego}
            onChange={handleChange}
            placeholder={t("plants.form.lastIrrigation")}
          />
          <input
            className="input"
            name="frecuenciaRiegoDias"
            value={form.frecuenciaRiegoDias}
            onChange={handleChange}
            placeholder={t("plants.form.irrigationFreq")}
          />
          <input
            className="input"
            type="date"
            name="fechaUltimaFertilizacion"
            value={form.fechaUltimaFertilizacion}
            onChange={handleChange}
            placeholder={t("plants.form.lastFertilization")}
          />
          <input
            className="input"
            name="frecuenciaFertilizacionDias"
            value={form.frecuenciaFertilizacionDias}
            onChange={handleChange}
            placeholder={t("plants.form.fertilizationFreq")}
          />
          <select
            className="select"
            name="estadoActual"
            value={form.estadoActual}
            onChange={handleChange}
          >
            <option value="OPTIMO">{t("status.optimo")}</option>
            <option value="VIGILANCIA">{t("status.vigilancia")}</option>
            <option value="CRITICO">{t("status.critico")}</option>
          </select>
          <select
            className="select"
            name="activo"
            value={form.activo}
            onChange={handleChange}
          >
            <option value="true">{t("common.active")}</option>
            <option value="false">{t("common.inactive")}</option>
          </select>
          <input
            className="input"
            name="observaciones"
            value={form.observaciones}
            onChange={handleChange}
            placeholder={t("plants.form.notes")}
          />
        </div>
      </Modal>
    </div>
  );
}
