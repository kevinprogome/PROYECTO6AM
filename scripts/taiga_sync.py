"""
Project: GreenHouse Manager
File: taiga_sync.py
Description: Sync Taiga user stories from local JSON definitions.
Author: Equipo GreenHouse Manager
Date: 2026-05-06
Version: 1.0.0
"""
import json
import os
from typing import Any, Dict, List

from taiga_common import api_request, get_project, get_userstories, get_userstory_statuses


def load_story_defs() -> List[Dict[str, Any]]:
    path = os.getenv("TAIGA_STORIES_PATH", "scripts/taiga_stories.json")
    with open(path, "r", encoding="utf-8") as handle:
        data = json.load(handle)
    return data.get("stories", [])


def build_description(story: Dict[str, Any]) -> str:
    description = (story.get("description") or "").strip()
    acceptance = story.get("acceptance", [])
    if not acceptance:
        return description
    lines: List[str] = []
    if description:
        lines.append(description)
        lines.append("")
    lines.append("Acceptance Criteria:")
    for item in acceptance:
        lines.append(f"- {item}")
    return "\n".join(lines).strip()


def main() -> None:
    stories = load_story_defs()
    if not stories:
        raise SystemExit("No stories found in scripts/taiga_stories.json")

    project = get_project()
    project_id = project["id"]

    status_map = get_userstory_statuses(project_id)
    existing = get_userstories(project_id)
    existing_map = {story["subject"].strip().lower(): story for story in existing}

    created = 0
    updated = 0

    for story in stories:
        subject = story.get("subject", "").strip()
        if not subject:
            continue
        description = build_description(story)
        tags = story.get("tags") or []
        status_name = story.get("status")
        status_id = status_map.get(status_name) if status_name else None

        payload: Dict[str, Any] = {
            "subject": subject,
            "description": description,
            "tags": tags
        }
        if status_id:
            payload["status"] = status_id

        existing_story = existing_map.get(subject.lower())
        if existing_story:
            api_request("PATCH", f"userstories/{existing_story['id']}", json=payload)
            updated += 1
        else:
            payload["project"] = project_id
            api_request("POST", "userstories", json=payload)
            created += 1

    print(f"Taiga sync complete. Created: {created}, Updated: {updated}")


if __name__ == "__main__":
    main()
