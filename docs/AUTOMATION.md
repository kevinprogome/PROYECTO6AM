# Automation Guide

This document explains how to run the automated tests, CI jobs, and local Docker stack for GreenHouse Manager.

## Test Layers

- Backend unit tests (JUnit + Mockito) use an in-memory H2 database with the `test` profile.
- Frontend unit tests run with Vitest.
- API tests run with pytest and require a running backend.
- Selenium UI tests run locally and require a running backend and frontend.

## Quick Commands

```bash
make backend-test
make frontend-test
make api-test
```

If you do not have GNU Make:

```bash
cd backend && mvn -B test
cd frontend && npm ci && npm run test:run
pytest -q
```

## Backend Tests

The Maven Surefire configuration sets `spring.profiles.active=test` and excludes Selenium tests by default.

```bash
cd backend
mvn -B test
```

## Frontend Tests

```bash
cd frontend
npm ci
npm run test:run
```

## API Tests (pytest)

1. Start the backend with the test profile.
2. Run pytest from the repository root.

```bash
cd backend
mvn -B -DskipTests spring-boot:run \
	-Dspring-boot.run.profiles=test \
	-Dspring-boot.run.useTestClasspath=true
```

In another terminal:

```bash
pytest -q
```

Environment variables supported by pytest:

- `API_URL` (default `http://localhost:8080`)
- `TEST_USER_EMAIL` (default `qa@greenhouse.local`)
- `TEST_USER_ROLE` (default `ADMIN`)

## Selenium UI Tests

Selenium tests are excluded from the default Maven test run. To execute them locally:

```bash
cd backend
mvn -B -Dsurefire.excludes= -Dtest=*SeleniumTest test
```

Make sure the backend and frontend are running before executing the Selenium suite.

## Docker Stack

Use Docker Compose to run MySQL, backend, and frontend.

```bash
docker compose up --build
```

Environment variables are provided in `.env` and `.env.example`.

## CI Workflow

The GitHub Actions workflow runs:

- Backend tests (`mvn test`)
- Frontend tests (`npm run test:run`)
- API tests (pytest + bootstrapped backend)

## Taiga Automation

The Taiga scripts read from `scripts/taiga_stories.json` and require the following env vars:

- `TAIGA_API_URL`
- `TAIGA_PROJECT`
- `TAIGA_TOKEN`

Commands:

```bash
python scripts/taiga_sync.py
python scripts/taiga_validator.py
python scripts/taiga_report.py
```
