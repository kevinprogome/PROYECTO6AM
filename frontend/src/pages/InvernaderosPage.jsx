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
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
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
    setForm(emptyForm);
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
  };

  /**
   * Saves the greenhouse changes.
   */
  const handleSave = async () => {
    const payload = {
      usuarioId: Number(form.usuarioId),
      nombre: form.nombre,
      ubicacion: form.ubicacion,
      descripcion: form.descripcion || null,
      areaM2: form.areaM2 ? Number(form.areaM2) : null
    };

    if (editingId) {
      await invernaderoService.update(editingId, payload);
    } else {
      await invernaderoService.create(payload);
    }

    setModalOpen(false);
    fetchData();
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
    await invernaderoService.delete(row.id);
    fetchData();
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
      {loading ? (
        <p className="muted">{t("common.loading")}</p>
      ) : error ? (
        <p className="muted">{error}</p>
      ) : (
        <DataTable columns={columns} rows={items} />
      )}
      <Modal
        isOpen={modalOpen}
        title={editingId ? t("greenhouses.edit") : t("greenhouses.create")}
        onClose={() => setModalOpen(false)}
        onSubmit={handleSave}
      >
        <div className="form-grid">
          <input
            className="input"
            name="usuarioId"
            value={form.usuarioId}
            onChange={handleChange}
            placeholder={t("greenhouses.form.ownerId")}
          />
          <input
            className="input"
            name="nombre"
            value={form.nombre}
            onChange={handleChange}
            placeholder={t("greenhouses.form.name")}
          />
          <input
            className="input"
            name="ubicacion"
            value={form.ubicacion}
            onChange={handleChange}
            placeholder={t("greenhouses.form.location")}
          />
          <input
            className="input"
            name="areaM2"
            value={form.areaM2}
            onChange={handleChange}
            placeholder={t("greenhouses.form.area")}
          />
          <input
            className="input"
            name="descripcion"
            value={form.descripcion}
            onChange={handleChange}
            placeholder={t("greenhouses.form.description")}
          />
        </div>
      </Modal>
    </div>
  );
}
