# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

The source for **pablosaraiva.com**, a personal site. A Spring Boot 3.5 (Java 17) backend serves a VuePress 1.x-generated static frontend. The frontend is a static blog (built with `@vuepress/plugin-blog`) whose output is bundled *into* the Spring Boot jar as static resources and served by the same process, with nginx as a reverse proxy in front in dev/prod.

## Build & run

The frontend (VuePress) and backend (Maven) are separate toolchains that meet in the Docker build.

```bash
# Backend only — compile, run tests, package jar (Java 17 / Spring Boot 3.5.x)
./mvnw clean install
./mvnw spring-boot:run                 # dev server on :8080 (serves whatever is currently in src/main/resources/static)

# Run a single test
./mvnw test -Dtest=WebApplicationTests#contextLoads

# Frontend only — build VuePress into src/main/resources/static (then run backend to view)
./buildvue.sh                          # vuepress build docs → copies dist/* into src/main/resources/static/
./debugvue.sh                          # vuepress dev docs (hot-reload dev server; bypasses Spring Boot)

# Full app via Docker (multi-stage: builds VuePress, copies dist into the jar, runs the jar)
docker build . -t pablosaraiva/web:latest
```

`buildvue.sh` deletes `static/assets` before copying so stale hashed assets don't accumulate — preserve that step when editing it. `debugvue.sh` runs the VuePress dev server standalone and does *not* go through Spring Boot, so `VuePressController` routes are irrelevant during pure frontend iteration.

## Architecture

### Two-stage content pipeline
1. VuePress builds `src/main/vuepress/docs/` (Markdown content + `.vuepress/config.js`) into `docs/.vuepress/dist/`.
2. That dist is placed into `src/main/resources/static/` (locally via `buildvue.sh`, or in Docker by copying the first stage's output there before `mvn install`). Spring Boot then serves it as static resources.

This means **edits to Markdown go in `src/main/vuepress/docs/`**, never directly in `src/main/resources/static/` — that directory is generated and will be overwritten on the next build.

### Backend (Spring Boot)
- `WebApplication` — `@SpringBootApplication` entry point.
- `VuePressController` — maps the VuePress frontend routes to `index.html` so deep links resolve server-side. **Each route is declared in both slash and no-slash forms** (`/about` and `/about/`, plus `/posts/page/*` and `/posts/page/*/`, and `/2*/*/*/*` and `/2*/*/*/*/` for dated post permalinks). Spring Framework 6 / Spring Boot 3 disabled trailing-slash matching by default (it was on in Spring Boot 2); VuePress's nav and sitemap use trailing-slash URLs, so both forms are listed explicitly rather than relying on `setUseTrailingSlashMatch` (deprecated in Spring 6, removed in Spring 7). **When adding a new top-level frontend route, add both slash forms to the `@GetMapping` list here** or it will 404. Only GET/HEAD are served. `RoutingTests` guards this behavior.
- `RedirectController` — server-side redirect for `/cats` (external link).
- `RedirectController` — server-side redirect for `/cats` (external link).

### Nginx layer
nginx `try_files $uri $uri/ @backend` serves static assets directly and falls back to Spring Boot (`http://web:8080` prod / `http://web-dev:8080` dev) for dynamic routes. Three configs: `nginx.conf` (plain HTTP, local), `nginx-dev.conf` and `nginx-prod.conf` (HTTPS/TLS with Let's Encrypt certs mounted from the host, plus HSTS and the ACME challenge location). Dev proxies `web-dev`, prod proxies `web`.

## Deployment (CircleCI)

`.circleci/config.yml` builds and pushes Docker images to Docker Hub (`pablosaraiva/web`):
- **`develop` branch** → `build-staging` → `pablosaraiva/web:latest`, after `test` (Maven, on `cimg/openjdk:17.0`) passes.
- **`master` branch** → `build-prod` → `pablosaraiva/web:stable`, after `test` passes.

`docker-compose-dev.yml` uses `:latest`; `docker-compose-prod.yml` uses `:stable`. The dev and prod compose files mount Let's Encrypt certs and an ACME challenge file from the host via the `ACME_CHALLENGE_FILENAME` env var — these are deployment-host concerns, not part of the repo.

## Key gotchas

- Java 17 is pinned in `pom.xml` (`java.version`); the Dockerfile build stage uses `maven:3.9-eclipse-temurin-17` and the runtime stage is `FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine` (amd64-only, pinned explicitly so builds are deterministic on Apple Silicon — no need to pass `--platform` yourself) and runs as a non-root `app` user. Spring Boot 3 requires Java 17+. Keep these coordinated when bumping.
- VuePress 1.x (`^1.9.10`) and `@vuepress/plugin-blog` are installed from `package-lock.json` via `npm ci` in the Docker build (no global installs). Local frontend deps live in `src/main/vuepress/` (`npm install` run there for local dev). VuePress 1.x is in maintenance; many transitive npm vulns remain that only a VuePress 2 migration would clear.
- `target/`, `.idea/`, the VuePress `dist/` output, and `node_modules/` are gitignored — don't commit generated artifacts.