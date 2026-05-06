"""
Proyecto: GreenHouse Manager
Archivo: test_scheduler_integration.py
Descripcion: Pruebas integrales del scheduler de alertas.
Autor: Equipo GreenHouse Manager
Fecha: 2026-05-06
Version: 1.0.0
"""
import requests


def test_trigger_scheduler_genera_alertas(base_url, auth_headers):
    """Valida que el endpoint de trigger del scheduler retorna 200."""
    response = requests.post(
        f"{base_url}/api/admin/trigger-scheduler",
        headers=auth_headers,
        timeout=20
    )
    assert response.status_code == 200
    assert "total" in response.json()


def test_no_duplica_alertas_mismo_dia(base_url, auth_headers):
    """Valida que el scheduler no duplique alertas el mismo dia."""
    first = requests.post(
        f"{base_url}/api/admin/trigger-scheduler",
        headers=auth_headers,
        timeout=20
    )
    first.raise_for_status()
    second = requests.post(
        f"{base_url}/api/admin/trigger-scheduler",
        headers=auth_headers,
        timeout=20
    )
    second.raise_for_status()
    assert second.json().get("total", 0) == 0
