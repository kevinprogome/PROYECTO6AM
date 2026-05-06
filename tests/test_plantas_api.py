"""
Proyecto: GreenHouse Manager
Archivo: test_plantas_api.py
Descripcion: Pruebas API de plantas.
Autor: Equipo GreenHouse Manager
Fecha: 2026-05-06
Version: 1.0.0
"""
import requests


def test_get_all_plantas_retorna_200(base_url, auth_headers):
    """Valida que el listado de plantas responde 200."""
    response = requests.get(f"{base_url}/api/plantas", headers=auth_headers, timeout=15)
    assert response.status_code == 200


def test_crear_planta_retorna_201(base_url, auth_headers, auth_context):
    """Valida que crear una planta retorna 201."""
    greenhouse_payload = {
        "usuarioId": int(auth_context["user_id"]),
        "nombre": "Invernadero API",
        "ubicacion": "Lote 3",
        "descripcion": "Temporal",
        "areaM2": 80
    }
    greenhouse_response = requests.post(
        f"{base_url}/api/invernaderos",
        json=greenhouse_payload,
        headers=auth_headers,
        timeout=15
    )
    greenhouse_response.raise_for_status()
    greenhouse = greenhouse_response.json()

    payload = {
        "invernaderoId": greenhouse["id"],
        "nombreComun": "Albahaca",
        "nombreCientifico": "Ocimum basilicum",
        "variedad": "Genovesa",
        "frecuenciaRiegoDias": 1,
        "frecuenciaFertilizacionDias": 5,
        "estadoActual": "OPTIMO",
        "activo": True
    }
    response = requests.post(
        f"{base_url}/api/plantas",
        json=payload,
        headers=auth_headers,
        timeout=15
    )
    assert response.status_code == 201

    plant_id = response.json()["id"]
    requests.delete(f"{base_url}/api/plantas/{plant_id}", headers=auth_headers, timeout=15)
    requests.delete(
        f"{base_url}/api/invernaderos/{greenhouse['id']}",
        headers=auth_headers,
        timeout=15
    )


def test_crear_planta_sin_nombre_retorna_400(base_url, auth_headers, auth_context):
    """Valida que no se puede crear planta sin nombre."""
    greenhouse_payload = {
        "usuarioId": int(auth_context["user_id"]),
        "nombre": "Invernadero API",
        "ubicacion": "Lote 4",
        "descripcion": "Temporal",
        "areaM2": 60
    }
    greenhouse_response = requests.post(
        f"{base_url}/api/invernaderos",
        json=greenhouse_payload,
        headers=auth_headers,
        timeout=15
    )
    greenhouse_response.raise_for_status()
    greenhouse = greenhouse_response.json()

    payload = {
        "invernaderoId": greenhouse["id"],
        "frecuenciaRiegoDias": 1,
        "frecuenciaFertilizacionDias": 5,
        "estadoActual": "OPTIMO",
        "activo": True
    }
    response = requests.post(
        f"{base_url}/api/plantas",
        json=payload,
        headers=auth_headers,
        timeout=15
    )
    assert response.status_code == 400

    requests.delete(
        f"{base_url}/api/invernaderos/{greenhouse['id']}",
        headers=auth_headers,
        timeout=15
    )


def test_obtener_planta_inexistente_retorna_404(base_url, auth_headers):
    """Valida que consultar una planta inexistente retorna 404."""
    response = requests.get(f"{base_url}/api/plantas/999999", headers=auth_headers, timeout=15)
    assert response.status_code == 404


def test_actualizar_planta_exitoso(base_url, auth_headers, planta_creada):
    """Valida que actualizar una planta retorna 200 y datos nuevos."""
    plant_id = planta_creada["planta"]["id"]
    payload = {
        "invernaderoId": planta_creada["invernadero"]["id"],
        "nombreComun": "Lechuga QA Actualizada",
        "nombreCientifico": "Lactuca sativa",
        "variedad": "Romaine",
        "frecuenciaRiegoDias": 3,
        "frecuenciaFertilizacionDias": 10,
        "estadoActual": "VIGILANCIA",
        "activo": True
    }
    response = requests.put(
        f"{base_url}/api/plantas/{plant_id}",
        json=payload,
        headers=auth_headers,
        timeout=15
    )
    assert response.status_code == 200
    assert response.json()["nombreComun"] == "Lechuga QA Actualizada"


def test_eliminar_planta_exitoso(base_url, auth_headers, auth_context):
    """Valida que eliminar una planta retorna 204."""
    greenhouse_payload = {
        "usuarioId": int(auth_context["user_id"]),
        "nombre": "Invernadero API",
        "ubicacion": "Lote 5",
        "descripcion": "Temporal",
        "areaM2": 70
    }
    greenhouse_response = requests.post(
        f"{base_url}/api/invernaderos",
        json=greenhouse_payload,
        headers=auth_headers,
        timeout=15
    )
    greenhouse_response.raise_for_status()
    greenhouse = greenhouse_response.json()

    payload = {
        "invernaderoId": greenhouse["id"],
        "nombreComun": "Menta",
        "frecuenciaRiegoDias": 2,
        "frecuenciaFertilizacionDias": 6,
        "estadoActual": "OPTIMO",
        "activo": True
    }
    response = requests.post(
        f"{base_url}/api/plantas",
        json=payload,
        headers=auth_headers,
        timeout=15
    )
    response.raise_for_status()
    plant_id = response.json()["id"]

    delete_response = requests.delete(
        f"{base_url}/api/plantas/{plant_id}",
        headers=auth_headers,
        timeout=15
    )
    assert delete_response.status_code == 204

    requests.delete(
        f"{base_url}/api/invernaderos/{greenhouse['id']}",
        headers=auth_headers,
        timeout=15
    )


def test_sin_token_retorna_401(base_url):
    """Valida que la API protege el acceso sin token."""
    response = requests.get(f"{base_url}/api/plantas", timeout=15)
    assert response.status_code in (401, 403)
