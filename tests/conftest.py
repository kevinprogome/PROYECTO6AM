"""
Proyecto: GreenHouse Manager
Archivo: conftest.py
Descripcion: Fixtures base para pruebas API con pytest.
Autor: Equipo GreenHouse Manager
Fecha: 2026-05-06
Version: 1.0.0
"""
import os
from typing import Dict

import pytest
import requests
from dotenv import load_dotenv

load_dotenv()


def pytest_configure() -> None:
    """Ensure test results directory exists."""
    os.makedirs("test-results", exist_ok=True)


@pytest.fixture(scope="session")
def base_url() -> str:
    """Provide API base URL from environment."""
    return os.getenv("API_URL", "http://localhost:8080").rstrip("/")


def _fetch_test_auth_context(base_url: str) -> Dict[str, str]:
    """Request a test JWT token from the backend."""
    payload = {
        "email": os.getenv("TEST_USER_EMAIL", "qa@greenhouse.local"),
        "role": os.getenv("TEST_USER_ROLE", "ADMIN")
    }
    response = requests.post(
        f"{base_url}/api/auth/test-token",
        json=payload,
        timeout=15
    )
    response.raise_for_status()
    data = response.json()
    if "token" not in data or "userId" not in data:
        raise RuntimeError("Missing token or userId in test auth response.")
    return {"token": data["token"], "user_id": str(data["userId"])}


@pytest.fixture(scope="session")
def auth_context(base_url: str) -> Dict[str, str]:
    """Return token and user id for authenticated API calls."""
    token = os.getenv("API_TEST_TOKEN")
    user_id = os.getenv("TEST_USER_ID")
    if token and user_id:
        return {"token": token, "user_id": user_id}
    return _fetch_test_auth_context(base_url)


@pytest.fixture(scope="session")
def auth_headers(auth_context: Dict[str, str]) -> Dict[str, str]:
    """Build authorization headers using a JWT token."""
    return {
        "Authorization": f"Bearer {auth_context['token']}",
        "Content-Type": "application/json"
    }


@pytest.fixture()
def planta_creada(base_url: str, auth_headers: Dict[str, str], auth_context: Dict[str, str]):
    """Create a greenhouse and plant for tests and cleanup after."""
    greenhouse_payload = {
        "usuarioId": int(auth_context["user_id"]),
        "nombre": "QA Invernadero",
        "ubicacion": "Lote QA",
        "descripcion": "Invernadero temporal",
        "areaM2": 120.5
    }
    greenhouse_response = requests.post(
        f"{base_url}/api/invernaderos",
        json=greenhouse_payload,
        headers=auth_headers,
        timeout=15
    )
    greenhouse_response.raise_for_status()
    greenhouse_data = greenhouse_response.json()

    plant_payload = {
        "invernaderoId": greenhouse_data["id"],
        "nombreComun": "Lechuga QA",
        "nombreCientifico": "Lactuca sativa",
        "variedad": "Crisp",
        "fechaSiembra": "2026-04-15",
        "fechaUltimoRiego": "2026-05-04",
        "frecuenciaRiegoDias": 2,
        "fechaUltimaFertilizacion": "2026-05-01",
        "frecuenciaFertilizacionDias": 7,
        "estadoActual": "OPTIMO",
        "observaciones": "Planta de prueba",
        "activo": True
    }
    plant_response = requests.post(
        f"{base_url}/api/plantas",
        json=plant_payload,
        headers=auth_headers,
        timeout=15
    )
    plant_response.raise_for_status()
    plant_data = plant_response.json()

    yield {"planta": plant_data, "invernadero": greenhouse_data}

    requests.delete(
        f"{base_url}/api/plantas/{plant_data['id']}",
        headers=auth_headers,
        timeout=15
    )
    requests.delete(
        f"{base_url}/api/invernaderos/{greenhouse_data['id']}",
        headers=auth_headers,
        timeout=15
    )
