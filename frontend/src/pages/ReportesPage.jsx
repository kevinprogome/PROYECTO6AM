/*
 * Proyecto: GreenHouse Manager
 * Archivo: ReportesPage.jsx
 * Descripcion: Pagina de reportes historicos.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useCallback, useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import DataTable from "../components/DataTable.jsx";
import { plantaService } from "../services/plantaService.js";
import { registroFertilizacionService } from "../services/registroFertilizacionService.js";
import { registroRiegoService } from "../services/registroRiegoService.js";

/**
 * Renders the reports page.
 *
 * @returns {JSX.Element} page component
 */
export default function ReportesPage() {
  const { t } = useTranslation();
  const [riegoItems, setRiegoItems] = useState([]);
  const [fertItems, setFertItems] = useState([]);
  const [plants, setPlants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const formatDateTime = (value) =>
    value ? new Date(value).toLocaleString() : t("common.notAvailable");

  /**
   * Loads reports data.
   */
  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const [riegoData, fertData, plantData] = await Promise.all([
        registroRiegoService.getAll(),
        registroFertilizacionService.getAll(),
        plantaService.getAll()
      ]);
      setRiegoItems(riegoData || []);
      setFertItems(fertData || []);
      setPlants(plantData || []);
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

  const plantMap = useMemo(() => {
    return new Map(plants.map((plant) => [plant.id, plant]));
  }, [plants]);

  const riegoRows = useMemo(() => {
    return riegoItems.map((item) => ({
      ...item,
      plantaNombre: plantMap.get(item.plantaId)?.nombreComun || item.plantaId
    }));
  }, [riegoItems, plantMap]);

  const fertRows = useMemo(() => {
    return fertItems.map((item) => ({
      ...item,
      plantaNombre: plantMap.get(item.plantaId)?.nombreComun || item.plantaId
    }));
  }, [fertItems, plantMap]);

  const riegoColumns = useMemo(
    () => [
      { key: "plantaNombre", header: t("reports.plant") },
      {
        key: "fechaRiego",
        header: t("reports.irrigation.date"),
        render: (row) => formatDateTime(row.fechaRiego)
      },
      { key: "volumenLitros", header: t("reports.irrigation.volume") },
      { key: "metodo", header: t("reports.irrigation.method") },
      { key: "responsable", header: t("reports.irrigation.operator") }
    ],
    [t]
  );

  const fertilizacionColumns = useMemo(
    () => [
      { key: "plantaNombre", header: t("reports.plant") },
      {
        key: "fechaFertilizacion",
        header: t("reports.fertilization.date"),
        render: (row) => formatDateTime(row.fechaFertilizacion)
      },
      { key: "tipoFertilizante", header: t("reports.fertilization.type") },
      { key: "dosis", header: t("reports.fertilization.dose") },
      { key: "unidad", header: t("reports.fertilization.unit") },
      { key: "responsable", header: t("reports.fertilization.operator") }
    ],
    [t]
  );

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">{t("reports.title")}</h1>
      </div>
      {loading ? (
        <p className="muted">{t("common.loading")}</p>
      ) : error ? (
        <p className="muted">{error}</p>
      ) : (
        <div className="grid" style={{ gap: 24 }}>
          <div>
            <h3>{t("reports.irrigationHistory")}</h3>
            <DataTable
              columns={riegoColumns}
              rows={riegoRows}
              emptyMessage={t("reports.emptyIrrigation")}
            />
          </div>
          <div>
            <h3>{t("reports.fertilizationHistory")}</h3>
            <DataTable
              columns={fertilizacionColumns}
              rows={fertRows}
              emptyMessage={t("reports.emptyFertilization")}
            />
          </div>
        </div>
      )}
    </div>
  );
}
