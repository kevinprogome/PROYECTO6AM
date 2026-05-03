/*
 * Proyecto: GreenHouse Manager
 * Archivo: Modal.jsx
 * Descripcion: Modal reutilizable para formularios CRUD.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
import { useTranslation } from "react-i18next";

/**
 * Renders a modal dialog.
 *
 * @param {Object} props component props
 * @param {boolean} props.isOpen modal visibility
 * @param {string} props.title modal title
 * @param {import("react").ReactNode} props.children modal content
 * @param {() => void} props.onClose close handler
 * @param {() => void} [props.onSubmit] submit handler
 * @param {string} [props.submitLabel] submit label
 * @returns {JSX.Element|null} modal component
 */
export default function Modal({
  isOpen,
  title,
  children,
  onClose,
  onSubmit,
  submitLabel
}) {
  const { t } = useTranslation();

  if (!isOpen) {
    return null;
  }

  /**
   * Handles backdrop click to close the modal.
   *
   * @param {import("react").MouseEvent<HTMLDivElement>} event click event
   */
  const handleBackdrop = (event) => {
    if (event.target === event.currentTarget) {
      onClose();
    }
  };

  return (
    <div className="modal-backdrop" onClick={handleBackdrop}>
      <div className="modal">
        <div className="modal-header">
          <h3 className="modal-title">{title}</h3>
          <button className="btn btn-outline" type="button" onClick={onClose}>
            {t("common.close")}
          </button>
        </div>
        <div>{children}</div>
        {onSubmit ? (
          <div className="modal-actions">
            <button className="btn btn-outline" type="button" onClick={onClose}>
              {t("common.cancel")}
            </button>
            <button className="btn btn-primary" type="button" onClick={onSubmit}>
              {submitLabel || t("common.save")}
            </button>
          </div>
        ) : null}
      </div>
    </div>
  );
}
