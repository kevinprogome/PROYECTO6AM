"""
Project: GreenHouse Manager
File: taiga_common.py
Description: Shared helpers for Taiga automation scripts.
Author: Equipo GreenHouse Manager
Date: 2026-05-06
Version: 1.0.0
"""
import os
from typing import Any, Dict, List

import requests
from dotenv import load_dotenv

load_dotenv()


def get_env(name: str, default: str | None = None, required: bool = False) -> str:
    """Read environment variables with optional defaults."""
    value = os.getenv(name, default)
    if required and not value:
        raise SystemExit(f"Missing env var: {name}")
    return value or ""


API_URL = get_env("TAIGA_API_URL", "https://api.taiga.io/api/v1").rstrip("/")
TOKEN = get_env("TAIGA_TOKEN", required=True)
PROJECT = get_env("TAIGA_PROJECT", required=True)
UI_URL = get_env("TAIGA_UI_URL", "https://tree.taiga.io").rstrip("/")


def _headers() -> Dict[str, str]:
    return {
        "Authorization": f"Bearer {TOKEN}",
        "Content-Type": "application/json"
    }


def api_request(method: str, path: str, **kwargs) -> Any:
    """Perform a Taiga API request and return JSON payload."""
    url = f"{API_URL}/{path.lstrip('/') }"
    headers = kwargs.pop("headers", {})
    headers.update(_headers())
    timeout = kwargs.pop("timeout", 20)
    response = requests.request(method, url, headers=headers, timeout=timeout, **kwargs)
    if response.status_code >= 400:
        try:
            detail = response.json()
        except ValueError:
            detail = response.text
        raise SystemExit(f"Taiga API error {response.status_code}: {detail}")
    if response.status_code == 204:
        return None
    return response.json()


def get_project() -> Dict[str, Any]:
    """Resolve project info by id or slug."""
    if PROJECT.isdigit():
        return api_request("GET", f"projects/{PROJECT}")
    return api_request("GET", "projects/by_slug", params={"project": PROJECT})


def get_userstory_statuses(project_id: int) -> Dict[str, int]:
    """Fetch user story status name to id map."""
    statuses = api_request("GET", "userstory-statuses", params={"project": project_id})
    return {status["name"]: status["id"] for status in statuses}


def get_userstories(project_id: int) -> List[Dict[str, Any]]:
    """Fetch user stories for a project."""
    return api_request("GET", "userstories", params={"project": project_id})


def build_story_url(project_slug: str, ref: int) -> str:
    """Build the Taiga UI url for a user story."""
    return f"{UI_URL}/project/{project_slug}/us/{ref}"
