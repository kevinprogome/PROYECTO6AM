"""
Proyecto: GreenHouse Manager
Archivo: test_alertas_api.py
Descripcion: Pruebas API de alertas.
Autor: Equipo GreenHouse Manager
Fecha: 2026-05-06
Version: 1.0.0
"""
import requests


def test_get_alertas_activas(base_url, auth_headers):
    """Valida que el endpoint de alertas responde 200."""
    response = requests.get(f"{base_url}/api/alertas", headers=auth_headers, timeout=15)
    assert response.status_code == 200


def test_resolver_alerta(base_url, auth_headers, planta_creada):
    """Valida que una alerta puede resolverse correctamente."""
    plant_id = planta_creada["planta"]["id"]
    greenhouse_id = planta_creada["invernadero"]["id"]
    payload = {
        "plantaId": plant_id,
        "invernaderoId": greenhouse_id,
        "tipo": "RIEGO",
        "severidad": "MEDIA",
        "mensaje": "alerta.riego.pendiente",
        "activa": True
    }
    response = requests.post(
        f"{base_url}/api/alertas",
        json=payload,
        headers=auth_headers,
        timeout=15
    )
    response.raise_for_status()
    alerta = response.json()

    update_payload = {
        "plantaId": plant_id,
        "invernaderoId": greenhouse_id,
        "tipo": alerta["tipo"],
        "severidad": alerta["severidad"],
        "mensaje": alerta["mensaje"],
        "activa": False
    }
    update_response = requests.put(
        f"{base_url}/api/alertas/{alerta['id']}",
        json=update_payload,
        headers=auth_headers,
        timeout=15
    )
    assert update_response.status_code == 200
    assert update_response.json()["activa"] is False


def test_alertas_filtradas_por_invernadero(base_url, auth_headers, planta_creada):
    """Valida que se puedan filtrar alertas activas por invernadero."""
    plant_id = planta_creada["planta"]["id"]
    greenhouse_id = planta_creada["invernadero"]["id"]
    payload = {
        "plantaId": plant_id,
        "invernaderoId": greenhouse_id,
        "tipo": "FERTILIZACION",
        "severidad": "MEDIA",
        "mensaje": "alerta.fertilizacion.pendiente",
        "activa": True
    }
    response = requests.post(
        f"{base_url}/api/alertas",
        json=payload,
        headers=auth_headers,
        timeout=15
    )
    response.raise_for_status()

    filtered = requests.get(
        f"{base_url}/api/alertas/activos/invernadero/{greenhouse_id}",
        headers=auth_headers,
        timeout=15
    )
    assert filtered.status_code == 200
    assert any(alerta["invernaderoId"] == greenhouse_id for alerta in filtered.json())
