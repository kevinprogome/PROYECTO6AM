"""
Project: GreenHouse Manager
File: taiga_report.py
Description: Generate a Taiga status report in Markdown.
Author: Equipo GreenHouse Manager
Date: 2026-05-06
Version: 1.0.0
"""
import os
from collections import Counter
from datetime import datetime
from typing import Any, Dict, List

from taiga_common import build_story_url, get_project, get_userstories


def get_status_name(story: Dict[str, Any]) -> str:
    info = story.get("status_extra_info")
    if isinstance(info, dict) and info.get("name"):
        return info["name"]
    return str(story.get("status", "Unknown"))


def main() -> None:
    project = get_project()
    project_id = project["id"]
    project_slug = project.get("slug") or str(project_id)

    stories = get_userstories(project_id)
    status_counts = Counter(get_status_name(story) for story in stories)

    report_path = os.getenv("TAIGA_REPORT_PATH", "docs/TAIGA.md")
    os.makedirs(os.path.dirname(report_path), exist_ok=True)

    lines: List[str] = []
    lines.append("# Taiga Report")
    lines.append("")
    lines.append(f"Project: {project.get('name', project_slug)}")
    lines.append(f"Generated: {datetime.utcnow().strftime('%Y-%m-%d')} UTC")
    lines.append("")
    lines.append("## Summary")
    lines.append("")
    lines.append(f"Total stories: {len(stories)}")
    for status, count in status_counts.items():
        lines.append(f"- {status}: {count}")

    lines.append("")
    lines.append("## Stories")
    lines.append("")
    lines.append("| Ref | Subject | Status | URL |")
    lines.append("| --- | --- | --- | --- |")

    for story in sorted(stories, key=lambda s: s.get("ref", 0)):
        ref = story.get("ref", "-")
        subject = story.get("subject", "-")
        status = get_status_name(story)
        url = build_story_url(project_slug, story.get("ref", 0))
        lines.append(f"| {ref} | {subject} | {status} | {url} |")

    with open(report_path, "w", encoding="utf-8") as handle:
        handle.write("\n".join(lines) + "\n")

    print(f"Report written to {report_path}")


if __name__ == "__main__":
    main()
