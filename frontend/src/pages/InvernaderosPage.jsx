/*
 * Proyecto: GreenHouse Manager
 * Archivo: InvernaderosPage.jsx
 * Descripcion: Pagina CRUD de invernaderos.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useCallback, useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import DataTable from "../components/DataTable.jsx";
import Modal from "../components/Modal.jsx";
import { useAuth } from "../hooks/useAuth.js";
import { invernaderoService } from "../services/invernaderoService.js";

const emptyForm = {
  usuarioId: "",
  nombre: "",
  ubicacion: "",
  descripcion: "",
  areaM2: ""
};

/**
 * Renders the greenhouses CRUD page.
 *
 * @returns {JSX.Element} page component
 */
export default function InvernaderosPage() {
  const { t } = useTranslation();
  const { user } = useAuth();
  const isAdmin = user?.role === "ADMIN";
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [saveError, setSaveError] = useState("");
  const [formErrors, setFormErrors] = useState({});
  const [modalOpen, setModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState(emptyForm);

  /**
   * Loads greenhouse data.
   */
  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const data = await invernaderoService.getAll();
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
   * Opens the modal for creating a greenhouse.
   */
  const handleCreate = () => {
    setEditingId(null);
    setForm({ ...emptyForm, usuarioId: user?.id || "" });
    setFormErrors({});
    setModalOpen(true);
  };

  /**
   * Opens the modal for editing a greenhouse.
   *
   * @param {Object} row greenhouse row
   */
  const handleEdit = (row) => {
    setEditingId(row.id);
    setForm({
      usuarioId: row.usuarioId || "",
      nombre: row.nombre || "",
      ubicacion: row.ubicacion || "",
      descripcion: row.descripcion || "",
      areaM2: row.areaM2 || ""
    });
    setFormErrors({});
    setModalOpen(true);
  };

  /**
   * Handles input changes in the form.
   *
   * @param {import("react").ChangeEvent<HTMLInputElement>} event input event
   */
  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
    setFormErrors((current) => ({ ...current, [name]: "" }));
  };

  const validateForm = () => {
    const errors = {};
    const resolvedUserId = form.usuarioId || user?.id;
    if (!resolvedUserId) {
      errors.usuarioId = t("greenhouses.validation.ownerRequired");
    }
    if (!form.nombre || !form.nombre.trim()) {
      errors.nombre = t("greenhouses.validation.nameRequired");
    }
    if (!form.ubicacion || !form.ubicacion.trim()) {
      errors.ubicacion = t("greenhouses.validation.locationRequired");
    }
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  /**
   * Saves the greenhouse changes.
   */
  const handleSave = async () => {
    setSaveError("");
    const resolvedUserId = form.usuarioId || user?.id;
    if (!validateForm()) {
      setSaveError(t("common.requiredFields"));
      return;
    }
    const payload = {
      usuarioId: Number(resolvedUserId),
      nombre: form.nombre.trim(),
      ubicacion: form.ubicacion.trim(),
      descripcion: form.descripcion || null,
      areaM2: form.areaM2 ? Number(form.areaM2) : null
    };
    try {
      if (editingId) {
        await invernaderoService.update(editingId, payload);
      } else {
        await invernaderoService.create(payload);
      }
      setModalOpen(false);
      fetchData();
    } catch (saveErrorResponse) {
      const apiMessage = saveErrorResponse?.response?.data?.message;
      setSaveError(apiMessage || t("common.saveError"));
    }
  };

  /**
   * Deletes a greenhouse.
   *
   * @param {Object} row greenhouse row
   */
  const handleDelete = async (row) => {
    if (!window.confirm(t("common.confirmDelete"))) {
      return;
    }
    try {
      await invernaderoService.delete(row.id);
      fetchData();
    } catch (deleteErrorResponse) {
      const apiMessage = deleteErrorResponse?.response?.data?.message;
      setSaveError(apiMessage || t("common.saveError"));
    }
  };

  /**
   * Renders row actions for the table.
   *
   * @param {Object} row greenhouse row
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
    </div>
  );

  const columns = useMemo(
    () => [
      { key: "usuarioId", header: t("greenhouses.table.ownerId") },
      { key: "nombre", header: t("greenhouses.table.name") },
      { key: "ubicacion", header: t("greenhouses.table.location") },
      { key: "areaM2", header: t("greenhouses.table.area") },
      { key: "actions", header: t("greenhouses.table.actions"), render: renderActions }
    ],
    [t]
  );

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">{t("greenhouses.title")}</h1>
        <button className="btn btn-primary" type="button" onClick={handleCreate}>
          {t("greenhouses.create")}
        </button>
      </div>
      {saveError ? <div className="notice notice-error">{saveError}</div> : null}
      {loading ? (
        <p className="muted">{t("common.loading")}</p>
      ) : error ? (
        <p className="muted">{error}</p>
      ) : (
        <DataTable columns={columns} rows={items} emptyMessage={t("greenhouses.empty")} />
      )}
      <Modal
        isOpen={modalOpen}
        title={editingId ? t("greenhouses.edit") : t("greenhouses.create")}
        onClose={() => setModalOpen(false)}
        onSubmit={handleSave}
      >
        <div className="form-grid">
          {isAdmin ? (
            <div className="field">
              <label className="field-label">
                {t("greenhouses.form.ownerId")} <span className="required">*</span>
              </label>
              <input
                className="input"
                name="usuarioId"
                value={form.usuarioId}
                onChange={handleChange}
                placeholder={t("greenhouses.form.ownerId")}
              />
              <p className="field-hint">{t("greenhouses.form.ownerHint")}</p>
              {formErrors.usuarioId ? (
                <p className="field-error">{formErrors.usuarioId}</p>
              ) : null}
            </div>
          ) : (
            <div className="field">
              <label className="field-label">{t("greenhouses.form.ownerLabel")}</label>
              <input
                className="input"
                value={user?.email || t("greenhouses.form.ownerFallback")}
                readOnly
              />
              <p className="field-hint">{t("greenhouses.form.ownerHint")}</p>
            </div>
          )}
          <div className="field">
            <label className="field-label">
              {t("greenhouses.form.name")} <span className="required">*</span>
            </label>
            <input
              className="input"
              name="nombre"
              value={form.nombre}
              onChange={handleChange}
              placeholder={t("greenhouses.form.name")}
            />
            <p className="field-hint">{t("greenhouses.form.nameHint")}</p>
            {formErrors.nombre ? (
              <p className="field-error">{formErrors.nombre}</p>
            ) : null}
          </div>
          <div className="field">
            <label className="field-label">
              {t("greenhouses.form.location")} <span className="required">*</span>
            </label>
            <input
              className="input"
              name="ubicacion"
              value={form.ubicacion}
              onChange={handleChange}
              placeholder={t("greenhouses.form.location")}
            />
            <p className="field-hint">{t("greenhouses.form.locationHint")}</p>
            {formErrors.ubicacion ? (
              <p className="field-error">{formErrors.ubicacion}</p>
            ) : null}
          </div>
          <div className="field">
            <label className="field-label">{t("greenhouses.form.area")}</label>
            <input
              className="input"
              type="number"
              min="0"
              step="0.01"
              name="areaM2"
              value={form.areaM2}
              onChange={handleChange}
              placeholder={t("greenhouses.form.area")}
            />
            <p className="field-hint">{t("greenhouses.form.areaHint")}</p>
          </div>
          <div className="field" style={{ gridColumn: "1 / -1" }}>
            <label className="field-label">{t("greenhouses.form.description")}</label>
            <textarea
              className="textarea"
              name="descripcion"
              value={form.descripcion}
              onChange={handleChange}
              placeholder={t("greenhouses.form.description")}
            />
            <p className="field-hint">{t("greenhouses.form.descriptionHint")}</p>
          </div>
        </div>
      </Modal>
    </div>
  );
}
