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
import { useAuth } from "../hooks/useAuth.js";
import { invernaderoService } from "../services/invernaderoService.js";
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

const emptyActionForm = {
  fecha: "",
  volumenLitros: "",
  metodo: "",
  responsable: "",
  notas: "",
  tipoFertilizante: "",
  dosis: "",
  unidad: ""
};

const toLocalDateTimeInput = (date) => {
  const offsetMs = date.getTimezoneOffset() * 60000;
  return new Date(date.getTime() - offsetMs).toISOString().slice(0, 16);
};

const toApiDateTime = (value) => {
  if (!value) {
    return null;
  }
  return value.length === 16 ? `${value}:00` : value;
};

/**
 * Renders the plants CRUD page.
 *
 * @returns {JSX.Element} page component
 */
export default function PlantasPage() {
  const { t } = useTranslation();
  const { user } = useAuth();
  const [items, setItems] = useState([]);
  const [greenhouses, setGreenhouses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [saveError, setSaveError] = useState("");
  const [formErrors, setFormErrors] = useState({});
  const [modalOpen, setModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState(emptyForm);
  const [estadoFilter, setEstadoFilter] = useState("");
  const [invernaderoFilter, setInvernaderoFilter] = useState("");
  const [searchTerm, setSearchTerm] = useState("");
  const [actionModal, setActionModal] = useState({
    open: false,
    type: "",
    plant: null
  });
  const [actionForm, setActionForm] = useState(emptyActionForm);
  const [actionError, setActionError] = useState("");

  /**
   * Loads plants data.
   */
  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const [plantData, greenhouseData] = await Promise.all([
        plantaService.getAll(),
        invernaderoService.getAll()
      ]);
      setItems(plantData || []);
      setGreenhouses(greenhouseData || []);
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
    const defaultGreenhouse = invernaderoFilter
      || (greenhouses.length === 1 ? String(greenhouses[0].id) : "");
    setForm({ ...emptyForm, invernaderoId: defaultGreenhouse });
    setFormErrors({});
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
    setFormErrors({});
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
    setFormErrors((current) => ({ ...current, [name]: "" }));
  };

  const validateForm = () => {
    const errors = {};
    if (!form.invernaderoId) {
      errors.invernaderoId = t("plants.validation.greenhouseRequired");
    }
    if (!form.nombreComun || !form.nombreComun.trim()) {
      errors.nombreComun = t("plants.validation.nameRequired");
    }
    const frecuenciaRiego = Number(form.frecuenciaRiegoDias);
    if (!frecuenciaRiego || frecuenciaRiego <= 0) {
      errors.frecuenciaRiegoDias = t("plants.validation.irrigationFreqRequired");
    }
    const frecuenciaFert = Number(form.frecuenciaFertilizacionDias);
    if (!frecuenciaFert || frecuenciaFert <= 0) {
      errors.frecuenciaFertilizacionDias = t("plants.validation.fertilizationFreqRequired");
    }
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  /**
   * Saves the plant changes.
   */
  const handleSave = async () => {
    setSaveError("");
    if (!validateForm()) {
      setSaveError(t("common.requiredFields"));
      return;
    }
    const payload = {
      invernaderoId: Number(form.invernaderoId),
      nombreComun: form.nombreComun.trim(),
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

    try {
      if (editingId) {
        await plantaService.update(editingId, payload);
      } else {
        await plantaService.create(payload);
      }
      setModalOpen(false);
      fetchData();
    } catch (saveErrorResponse) {
      const apiMessage = saveErrorResponse?.response?.data?.message;
      setSaveError(apiMessage || t("common.saveError"));
    }
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
    try {
      await plantaService.delete(row.id);
      fetchData();
    } catch (deleteErrorResponse) {
      const apiMessage = deleteErrorResponse?.response?.data?.message;
      setSaveError(apiMessage || t("common.saveError"));
    }
  };

  /**
   * Registers an irrigation record for a plant.
   *
   * @param {Object} row plant row
   */
  const openActionModal = (type, plant) => {
    setActionModal({ open: true, type, plant });
    setActionForm({
      ...emptyActionForm,
      fecha: toLocalDateTimeInput(new Date()),
      responsable: user?.email || ""
    });
    setActionError("");
  };

  const closeActionModal = () => {
    setActionModal({ open: false, type: "", plant: null });
    setActionForm(emptyActionForm);
    setActionError("");
  };

  const handleActionChange = (event) => {
    const { name, value } = event.target;
    setActionForm((current) => ({ ...current, [name]: value }));
  };

  const handleActionSubmit = async () => {
    if (!actionModal.plant) {
      return;
    }
    setActionError("");
    const actionDate = toApiDateTime(actionForm.fecha);
    if (!actionDate) {
      setActionError(t("plants.validation.actionDateRequired"));
      return;
    }

    try {
      if (actionModal.type === "RIEGO") {
        await registroRiegoService.create({
          plantaId: actionModal.plant.id,
          fechaRiego: actionDate,
          volumenLitros: actionForm.volumenLitros
            ? Number(actionForm.volumenLitros)
            : null,
          metodo: actionForm.metodo || null,
          responsable: actionForm.responsable || null,
          notas: actionForm.notas || null
        });
      } else {
        if (!actionForm.tipoFertilizante || !actionForm.tipoFertilizante.trim()) {
          setActionError(t("plants.validation.fertilizerRequired"));
          return;
        }
        await registroFertilizacionService.create({
          plantaId: actionModal.plant.id,
          fechaFertilizacion: actionDate,
          tipoFertilizante: actionForm.tipoFertilizante.trim(),
          dosis: actionForm.dosis ? Number(actionForm.dosis) : null,
          unidad: actionForm.unidad || null,
          responsable: actionForm.responsable || null,
          notas: actionForm.notas || null
        });
      }
      closeActionModal();
      fetchData();
    } catch (actionErrorResponse) {
      const apiMessage = actionErrorResponse?.response?.data?.message;
      setActionError(apiMessage || t("common.saveError"));
    }
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
      const term = searchTerm.trim().toLowerCase();
      const matchesSearch = term
        ? `${item.nombreComun} ${item.nombreCientifico || ""} ${item.variedad || ""}`
          .toLowerCase()
          .includes(term)
        : true;
      return statusOk && greenhouseOk && matchesSearch;
    });
  }, [items, estadoFilter, invernaderoFilter, searchTerm]);

  const greenhouseMap = useMemo(() => {
    return new Map(greenhouses.map((item) => [String(item.id), item]));
  }, [greenhouses]);

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
      <button className="btn btn-outline" type="button" onClick={() => openActionModal("RIEGO", row)}>
        {t("plants.registerIrrigation")}
      </button>
      <button
        className="btn btn-outline"
        type="button"
        onClick={() => openActionModal("FERTILIZACION", row)}
      >
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
      {
        key: "invernaderoId",
        header: t("plants.table.greenhouse"),
        render: (row) => {
          const greenhouse = greenhouseMap.get(String(row.invernaderoId));
          return greenhouse
            ? `${greenhouse.nombre} - ${greenhouse.ubicacion}`
            : row.invernaderoId;
        }
      },
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
    [t, greenhouseMap]
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
        <input
          className="input"
          value={searchTerm}
          onChange={(event) => setSearchTerm(event.target.value)}
          placeholder={t("plants.search")}
        />
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
        <select
          className="select"
          value={invernaderoFilter}
          onChange={(event) => setInvernaderoFilter(event.target.value)}
        >
          <option value="">{t("plants.filterGreenhouse")}</option>
          {greenhouses.map((greenhouse) => (
            <option key={greenhouse.id} value={greenhouse.id}>
              {greenhouse.nombre} - {greenhouse.ubicacion}
            </option>
          ))}
        </select>
      </div>
      {!loading && greenhouses.length === 0 ? (
        <div className="notice notice-info">{t("plants.noGreenhouses")}</div>
      ) : null}
      {saveError ? <div className="notice notice-error">{saveError}</div> : null}
      {loading ? (
        <p className="muted">{t("common.loading")}</p>
      ) : error ? (
        <p className="muted">{error}</p>
      ) : (
        <DataTable
          columns={columns}
          rows={filteredItems}
          emptyMessage={t("plants.empty")}
        />
      )}
      <Modal
        isOpen={modalOpen}
        title={editingId ? t("plants.edit") : t("plants.create")}
        onClose={() => setModalOpen(false)}
        onSubmit={handleSave}
      >
        <div className="form-section">
          <h4 className="form-section-title">{t("plants.sections.basic")}</h4>
          <div className="form-grid">
            <div className="field">
              <label className="field-label">
                {t("plants.form.greenhouseId")} <span className="required">*</span>
              </label>
              <select
                className="select"
                name="invernaderoId"
                value={form.invernaderoId}
                onChange={handleChange}
              >
                <option value="">{t("common.selectOption")}</option>
                {greenhouses.map((greenhouse) => (
                  <option key={greenhouse.id} value={greenhouse.id}>
                    {greenhouse.nombre} - {greenhouse.ubicacion}
                  </option>
                ))}
              </select>
              <p className="field-hint">{t("plants.form.greenhouseHint")}</p>
              {formErrors.invernaderoId ? (
                <p className="field-error">{formErrors.invernaderoId}</p>
              ) : null}
            </div>
            <div className="field">
              <label className="field-label">
                {t("plants.form.commonName")} <span className="required">*</span>
              </label>
              <input
                className="input"
                name="nombreComun"
                value={form.nombreComun}
                onChange={handleChange}
                placeholder={t("plants.form.commonName")}
              />
              <p className="field-hint">{t("plants.form.commonNameHint")}</p>
              {formErrors.nombreComun ? (
                <p className="field-error">{formErrors.nombreComun}</p>
              ) : null}
            </div>
            <div className="field">
              <label className="field-label">{t("plants.form.scientificName")}</label>
              <input
                className="input"
                name="nombreCientifico"
                value={form.nombreCientifico}
                onChange={handleChange}
                placeholder={t("plants.form.scientificName")}
              />
              <p className="field-hint">{t("plants.form.scientificNameHint")}</p>
            </div>
            <div className="field">
              <label className="field-label">{t("plants.form.variety")}</label>
              <input
                className="input"
                name="variedad"
                value={form.variedad}
                onChange={handleChange}
                placeholder={t("plants.form.variety")}
              />
              <p className="field-hint">{t("plants.form.varietyHint")}</p>
            </div>
          </div>
        </div>
        <div className="form-section">
          <h4 className="form-section-title">{t("plants.sections.schedule")}</h4>
          <div className="form-grid">
            <div className="field">
              <label className="field-label">{t("plants.form.sowDate")}</label>
              <input
                className="input"
                type="date"
                name="fechaSiembra"
                value={form.fechaSiembra}
                onChange={handleChange}
              />
              <p className="field-hint">{t("plants.form.sowDateHint")}</p>
            </div>
            <div className="field">
              <label className="field-label">{t("plants.form.lastIrrigation")}</label>
              <input
                className="input"
                type="date"
                name="fechaUltimoRiego"
                value={form.fechaUltimoRiego}
                onChange={handleChange}
              />
              <p className="field-hint">{t("plants.form.lastIrrigationHint")}</p>
            </div>
            <div className="field">
              <label className="field-label">
                {t("plants.form.irrigationFreq")} <span className="required">*</span>
              </label>
              <input
                className="input"
                type="number"
                min="1"
                name="frecuenciaRiegoDias"
                value={form.frecuenciaRiegoDias}
                onChange={handleChange}
                placeholder={t("plants.form.irrigationFreq")}
              />
              <p className="field-hint">{t("plants.form.irrigationFreqHint")}</p>
              {formErrors.frecuenciaRiegoDias ? (
                <p className="field-error">{formErrors.frecuenciaRiegoDias}</p>
              ) : null}
            </div>
            <div className="field">
              <label className="field-label">{t("plants.form.lastFertilization")}</label>
              <input
                className="input"
                type="date"
                name="fechaUltimaFertilizacion"
                value={form.fechaUltimaFertilizacion}
                onChange={handleChange}
              />
              <p className="field-hint">{t("plants.form.lastFertilizationHint")}</p>
            </div>
            <div className="field">
              <label className="field-label">
                {t("plants.form.fertilizationFreq")} <span className="required">*</span>
              </label>
              <input
                className="input"
                type="number"
                min="1"
                name="frecuenciaFertilizacionDias"
                value={form.frecuenciaFertilizacionDias}
                onChange={handleChange}
                placeholder={t("plants.form.fertilizationFreq")}
              />
              <p className="field-hint">{t("plants.form.fertilizationFreqHint")}</p>
              {formErrors.frecuenciaFertilizacionDias ? (
                <p className="field-error">{formErrors.frecuenciaFertilizacionDias}</p>
              ) : null}
            </div>
          </div>
        </div>
        <div className="form-section">
          <h4 className="form-section-title">{t("plants.sections.status")}</h4>
          <div className="form-grid">
            <div className="field">
              <label className="field-label">{t("plants.form.status")}</label>
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
              <p className="field-hint">{t("plants.form.statusHint")}</p>
            </div>
            <div className="field">
              <label className="field-label">{t("plants.form.active")}</label>
              <select
                className="select"
                name="activo"
                value={form.activo}
                onChange={handleChange}
              >
                <option value="true">{t("common.active")}</option>
                <option value="false">{t("common.inactive")}</option>
              </select>
              <p className="field-hint">{t("plants.form.activeHint")}</p>
            </div>
          </div>
        </div>
        <div className="form-section">
          <h4 className="form-section-title">{t("plants.sections.notes")}</h4>
          <div className="field">
            <label className="field-label">{t("plants.form.notes")}</label>
            <textarea
              className="textarea"
              name="observaciones"
              value={form.observaciones}
              onChange={handleChange}
              placeholder={t("plants.form.notes")}
            />
            <p className="field-hint">{t("plants.form.notesHint")}</p>
          </div>
        </div>
      </Modal>
      <Modal
        isOpen={actionModal.open}
        title={
          actionModal.type === "RIEGO"
            ? t("plants.actions.irrigationTitle", { name: actionModal.plant?.nombreComun || "" })
            : t("plants.actions.fertilizationTitle", { name: actionModal.plant?.nombreComun || "" })
        }
        onClose={closeActionModal}
        onSubmit={handleActionSubmit}
        submitLabel={
          actionModal.type === "RIEGO"
            ? t("plants.actions.irrigationSubmit")
            : t("plants.actions.fertilizationSubmit")
        }
      >
        <div className="form-grid">
          <div className="field">
            <label className="field-label">
              {t("plants.actions.actionDate")} <span className="required">*</span>
            </label>
            <input
              className="input"
              type="datetime-local"
              name="fecha"
              value={actionForm.fecha}
              onChange={handleActionChange}
            />
          </div>
          {actionModal.type === "FERTILIZACION" ? (
            <div className="field">
              <label className="field-label">
                {t("plants.actions.fertilizerType")} <span className="required">*</span>
              </label>
              <input
                className="input"
                name="tipoFertilizante"
                value={actionForm.tipoFertilizante}
                onChange={handleActionChange}
                placeholder={t("plants.actions.fertilizerType")}
              />
            </div>
          ) : null}
          {actionModal.type === "RIEGO" ? (
            <div className="field">
              <label className="field-label">{t("plants.actions.volume")}</label>
              <input
                className="input"
                type="number"
                min="0"
                step="0.01"
                name="volumenLitros"
                value={actionForm.volumenLitros}
                onChange={handleActionChange}
                placeholder={t("plants.actions.volume")}
              />
            </div>
          ) : null}
          {actionModal.type === "FERTILIZACION" ? (
            <div className="field">
              <label className="field-label">{t("plants.actions.dose")}</label>
              <input
                className="input"
                type="number"
                min="0"
                step="0.01"
                name="dosis"
                value={actionForm.dosis}
                onChange={handleActionChange}
                placeholder={t("plants.actions.dose")}
              />
            </div>
          ) : null}
          {actionModal.type === "FERTILIZACION" ? (
            <div className="field">
              <label className="field-label">{t("plants.actions.unit")}</label>
              <input
                className="input"
                name="unidad"
                value={actionForm.unidad}
                onChange={handleActionChange}
                placeholder={t("plants.actions.unit")}
              />
            </div>
          ) : null}
          <div className="field">
            <label className="field-label">{t("plants.actions.method")}</label>
            <input
              className="input"
              name="metodo"
              value={actionForm.metodo}
              onChange={handleActionChange}
              placeholder={t("plants.actions.method")}
            />
          </div>
          <div className="field">
            <label className="field-label">{t("plants.actions.responsable")}</label>
            <input
              className="input"
              name="responsable"
              value={actionForm.responsable}
              onChange={handleActionChange}
              placeholder={t("plants.actions.responsable")}
            />
          </div>
          <div className="field" style={{ gridColumn: "1 / -1" }}>
            <label className="field-label">{t("plants.actions.notes")}</label>
            <textarea
              className="textarea"
              name="notas"
              value={actionForm.notas}
              onChange={handleActionChange}
              placeholder={t("plants.actions.notes")}
            />
          </div>
        </div>
        {actionError ? <div className="notice notice-error">{actionError}</div> : null}
      </Modal>
    </div>
  );
}
