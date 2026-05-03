/*
 * Proyecto: GreenHouse Manager
 * Archivo: DataTable.jsx
 * Descripcion: Tabla generica con paginacion.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useTranslation } from "react-i18next";
import { usePagination } from "../hooks/usePagination.js";

/**
 * @typedef {Object} DataColumn
 * @property {string} key field key
 * @property {string} header column header
 * @property {(row: Object) => import("react").ReactNode} [render] custom renderer
 */

/**
 * Renders a data table with basic pagination.
 *
 * @param {Object} props component props
 * @param {DataColumn[]} props.columns table columns
 * @param {Object[]} props.rows table rows
 * @param {number} [props.pageSize] items per page
 * @param {string} [props.emptyMessage] empty state message
 * @returns {JSX.Element} data table
 */
export default function DataTable({
  columns,
  rows,
  pageSize = 8,
  emptyMessage
}) {
  const { t } = useTranslation();
  const { page, totalPages, pageItems, nextPage, prevPage } = usePagination(
    rows,
    pageSize
  );

  /**
   * Handles previous page action.
   */
  const handlePrev = () => {
    prevPage();
  };

  /**
   * Handles next page action.
   */
  const handleNext = () => {
    nextPage();
  };

  if (!rows || rows.length === 0) {
    return <div className="muted">{emptyMessage || t("table.noData")}</div>;
  }

  return (
    <div className="table-card">
      <table className="data-table">
        <thead>
          <tr>
            {columns.map((column) => (
              <th key={column.key}>{column.header}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {pageItems.map((row, index) => (
            <tr key={row.id || index}>
              {columns.map((column) => (
                <td key={column.key}>
                  {column.render ? column.render(row) : row[column.key]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      <div className="table-footer">
        <span className="muted">
          {t("table.page")} {page} {t("table.of")} {totalPages}
        </span>
        <div className="table-actions">
          <button className="btn btn-outline" type="button" onClick={handlePrev}>
            {t("table.prev")}
          </button>
          <button className="btn btn-outline" type="button" onClick={handleNext}>
            {t("table.next")}
          </button>
        </div>
      </div>
    </div>
  );
}
