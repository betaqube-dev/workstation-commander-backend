# Repository Guidelines

This repository is a Spring Boot 3.5.8 service targeting Java 21. Keep changes small, documented, and runnable with the bundled Maven Wrapper.

## Project Structure & Module Organization
- `src/main/java/dev/betaqube/wc`: Application entrypoint `WorkstationCommanderBackendApplication`; add controllers/services here following the `dev.betaqube.wc` package.
- `src/main/resources`: `application.yaml` for defaults; `static/` and `templates/` are ready for web assets if needed.
- `src/test/java/dev/betaqube/wc`: JUnit 5 tests; mirror package layout of main code.
- Build outputs land in `target/`; keep generated artifacts out of source control.

## Build, Test, and Development Commands
- `./mvnw clean verify` — full build with tests.
- `./mvnw spring-boot:run` — run the app locally with dev-friendly defaults.
- `./mvnw test` — execute the test suite only.
- `./mvnw package -DskipTests` — create `target/workstation-commander-backend-0.0.1-SNAPSHOT.jar` when you need a quick artifact.
- Set `SPRING_PROFILES_ACTIVE` for profile-specific config; use `SPRING_APPLICATION_NAME` or `--spring.config.location` overrides instead of editing `application.yaml` for local secrets.

## Coding Style & Naming Conventions
- Java code uses 4-space indentation and UTF-8; keep lines ≤120 chars when practical.
- Package names are lowercase (`dev.betaqube.wc`); classes and enums are `PascalCase`; methods and fields use `camelCase`; test classes end with `*Tests`.
- Favor constructor injection for Spring beans; avoid field injection.
- Log via `org.slf4j.Logger` (no `System.out` in production paths).

## Testing Guidelines
- JUnit Jupiter via `spring-boot-starter-test` is available. Prefer fast, isolated unit tests; mark heavier integration tests with clear naming or tags.
- Place tests alongside matching packages under `src/test/java`; name test methods descriptively (e.g., `shouldHandleEmptyPayload`).
- Run `./mvnw test` before pushing; add assertions around expected responses, HTTP status codes, and error paths when endpoints are introduced.

## Commit & Pull Request Guidelines
- Git metadata is not bundled here; if using Git, keep commit subjects imperative and concise (e.g., `Add workstation bootstrap endpoint`). Always use Conventional Commits (`feat:`, `fix:`, etc.) for clarity.
- PRs should include a short summary, linked issue/ticket, test evidence (command output or notes), and any configuration or migration steps. Add screenshots or sample requests/responses if behavior changes.
- Only create commits when explicitly requested.

## Security & Configuration Tips
- Do not commit secrets or tokens; prefer environment variables or an untracked `application-*.yaml`.
- When adding new config, document defaults and overrides; avoid widening CORS or debug flags without justification.
