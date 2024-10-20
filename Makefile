help: ## Show this help
	@printf "\033[33m%s:\033[0m\n" 'Available commands'
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z0-9_-]+:.*?## / {printf "  \033[32m%-18s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

build: ## Build containers
	docker compose build

up: ## Run application
	docker compose up -d

down: ## Down application
	docker compose down

restart: down up ## Restart application

shell: ## Shell at clojure docker
	docker compose exec app bash

run: ## Run application
	docker compose exec app lein run -m patient.core

test: ## Run app tests
	docker compose exec app lein test

deps: ## Upload dependencies
	docker compose exec app lein deps

psql: ## PostgreSql shell
	docker compose exec db psql -U postgres -d patient_db

log: ## Show container logs
	docker compose logs -f
