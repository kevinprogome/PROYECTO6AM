SHELL := /bin/bash

.PHONY: help backend-test frontend-test api-test dev-backend dev-frontend docker-up docker-down docker-logs taiga-sync taiga-validate taiga-report

help:
	@echo "make backend-test   # run backend unit tests"
	@echo "make frontend-test  # run frontend unit tests"
	@echo "make api-test       # run pytest API tests (backend must be running)"
	@echo "make dev-backend    # run Spring Boot locally"
	@echo "make dev-frontend   # run Vite dev server"
	@echo "make docker-up      # start stack with Docker"
	@echo "make docker-down    # stop stack and remove volumes"
	@echo "make docker-logs    # tail Docker logs"
	@echo "make taiga-sync     # sync Taiga stories from JSON"
	@echo "make taiga-validate # validate Taiga stories"
	@echo "make taiga-report   # generate Taiga report"

backend-test:
	cd backend && mvn -B test

frontend-test:
	cd frontend && npm ci && npm run test:run

api-test:
	pytest -q

dev-backend:
	cd backend && mvn spring-boot:run

dev-frontend:
	cd frontend && npm run dev

docker-up:
	docker compose up --build

docker-down:
	docker compose down -v

docker-logs:
	docker compose logs -f --tail=200

taiga-sync:
	python scripts/taiga_sync.py

taiga-validate:
	python scripts/taiga_validator.py

taiga-report:
	python scripts/taiga_report.py
