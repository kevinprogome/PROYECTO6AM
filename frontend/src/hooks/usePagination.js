/*
 * Proyecto: GreenHouse Manager
 * Archivo: usePagination.js
 * Descripcion: Hook para paginacion de listas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useCallback, useEffect, useMemo, useState } from "react";

/**
 * Provides pagination helpers for a list of items.
 *
 * @param {Array} items list of items
 * @param {number} pageSize items per page
 * @returns {Object} pagination state and handlers
 */
export function usePagination(items, pageSize) {
  const safeItems = items || [];
  const size = pageSize || 10;
  const [page, setPage] = useState(1);

  const totalPages = useMemo(() => {
    return Math.max(1, Math.ceil(safeItems.length / size));
  }, [safeItems.length, size]);

  useEffect(() => {
    if (page > totalPages) {
      setPage(totalPages);
    }
  }, [page, totalPages]);

  const pageItems = useMemo(() => {
    const start = (page - 1) * size;
    return safeItems.slice(start, start + size);
  }, [safeItems, page, size]);

  /**
   * Moves to the next page if possible.
   */
  const nextPage = useCallback(() => {
    setPage((current) => Math.min(current + 1, totalPages));
  }, [totalPages]);

  /**
   * Moves to the previous page if possible.
   */
  const prevPage = useCallback(() => {
    setPage((current) => Math.max(current - 1, 1));
  }, []);

  /**
   * Sets a specific page within range.
   *
   * @param {number} value page number
   */
  const setPageSafe = useCallback(
    (value) => {
      const next = Math.min(Math.max(1, value), totalPages);
      setPage(next);
    },
    [totalPages]
  );

  return {
    page,
    totalPages,
    pageItems,
    nextPage,
    prevPage,
    setPage: setPageSafe
  };
}
