# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build all modules
mvn clean install

# Run the application (api module is the entry point)
mvn spring-boot:run -pl api

# Run all tests
mvn test

# Run tests for a specific module
mvn test -pl domain
mvn test -pl application
mvn test -pl infrastructure
mvn test -pl api

# Run a single test class
mvn test -pl api -Dtest=ApiApplicationTests
```

**Prerequisites:** PostgreSQL must be running at `localhost:5432` with database `cleanbank`, user `postgres`, password `postgres`.

Swagger UI is available at `http://localhost:8080/swagger-ui` after startup.

## Architecture Overview

This is a **4-module Maven project** implementing Onion/Clean Architecture for a banking application. The dependency direction is strictly inward:

```
api → application → domain
infrastructure → domain + application
api → infrastructure (for Spring Boot component scan)
```

### Module Responsibilities

| Module | Package | Role |
|--------|---------|------|
| `domain` | `com.devblo` | Aggregates, value objects, domain events, repository interfaces |
| `application` | `com.devblo` | CQRS command/query handlers, custom `Mediator` |
| `infrastructure` | `com.devblo.infrastructure` | JPA implementations, Flyway, RabbitMQ, Outbox |
| `api` | `com.devblo.api` | REST controllers, request/response DTOs, `ApiApplication` |

`ApiApplication` uses `@SpringBootApplication(scanBasePackages = "com.devblo")` to pick up beans from all modules.

## Key Patterns

### CQRS via Custom Mediator

Commands and queries are plain records implementing `ICommand<R>` or `IQuery<R>`. Handlers implement `ICommandHandler<C, R>` or `IQueryHandler<Q, R>`. The `Mediator` auto-discovers all handlers on startup via Spring's `List<ICommandHandler<?,?>>` injection and routes by exact type match.

```
Controller → mediator.sendCommand(new OpenAccountCommand(...)) → OpenAccountCommandHandler
Controller → mediator.sendQuery(new GetAccountBalanceQuery(...)) → GetAccountBalanceQueryHandler
```

Each command/query pair lives in its own sub-package (e.g., `account/command/openAccount/`).

### Result Pattern

Domain operations return `Result<T>` (a sealed interface with `Success<T>` and `Failure<T>` records) instead of throwing exceptions for business rule violations. Never throw exceptions for expected domain failures; use `Result.failure("message")`.

`BaseController.respond(result)` converts `Result<T>` → `ResponseEntity<ApiResponse<T>>` (200 OK or 400 Bad Request).

### Aggregate Roots & Domain Events

Aggregates extend `BaseAggregateRoot<Id>`, which provides `registerEvent()` and `getDomainEvents()`. Domain events are registered during state-changing operations (not thrown or dispatched immediately).

Events flow: `aggregate.registerEvent(event)` → `JpaWriteRepository.save()` → `DomainEventPublisher.publishAll(aggregate)` → Spring `ApplicationEventPublisher` → `@EventListener` in infrastructure → `OutboxEvent` persisted.

**Two static factory methods** exist on aggregates:
- `Account.open(...)` — creates new instances, registers domain events
- `Account.reconstitute(...)` — rebuilds from DB, does **not** register events

### Repository Split

Each aggregate has separate read and write repository interfaces in `domain`:
- `IAccountWriteRepository` — findById, save, delete (used by command handlers)
- `IAccountReadRepository` — findSummaryById returning `AccountSummary` read models (used by query handlers)

Infrastructure implements both: `JpaAccountWriteRepository` and `JpaAccountReadRepository`.

### Outbox Pattern

`OutboxEvent` entities are persisted in the same transaction as aggregate saves. `OutboxProcessor` (stub, in progress) will relay them to RabbitMQ. `RabbitMqConfig` is also a stub pending implementation.

## Adding New Use Cases

1. Create `Command`/`Query` record and its `Handler` in the appropriate `application` sub-package.
2. The `Mediator` auto-registers the handler — no manual wiring needed.
3. Add controller method in `api` that builds the command/query and calls `mediator.sendCommand()` / `mediator.sendQuery()`, then returns `respond(result)`.
