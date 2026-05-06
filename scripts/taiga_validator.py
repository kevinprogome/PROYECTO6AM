"""
Project: GreenHouse Manager
File: taiga_validator.py
Description: Validate Taiga stories against local definitions.
Author: Equipo GreenHouse Manager
Date: 2026-05-06
Version: 1.0.0
"""
import json
import os
from typing import Any, Dict, List

from taiga_common import api_request, get_project, get_userstories


def load_story_defs() -> List[Dict[str, Any]]:
    path = os.getenv("TAIGA_STORIES_PATH", "scripts/taiga_stories.json")
    with open(path, "r", encoding="utf-8") as handle:
        data = json.load(handle)
    return data.get("stories", [])


def main() -> None:
    stories = load_story_defs()
    project = get_project()
    existing = get_userstories(project["id"])
    existing_map = {story["subject"].strip().lower(): story for story in existing}

    errors: List[str] = []

    for story in stories:
        subject = story.get("subject", "").strip()
        if not subject:
            continue
        target = existing_map.get(subject.lower())
        if not target:
            errors.append(f"Missing story in Taiga: {subject}")
            continue

        acceptance = story.get("acceptance", [])
        description = target.get("description") or ""
        if not description:
            detail = api_request("GET", f"userstories/{target['id']}")
            description = detail.get("description") or detail.get("description_html") or ""
        for item in acceptance:
            if item not in description:
                errors.append(f"Missing acceptance criteria for: {subject}")
                break

    if errors:
        print("Taiga validation failed:")
        for error in errors:
            print(f"- {error}")
        raise SystemExit(1)

    print("Taiga validation passed.")


if __name__ == "__main__":
    main()
