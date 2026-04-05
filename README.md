# CleanBank - Core Banking Platform

A full-stack banking application built with **Clean Architecture** principles, featuring a Spring Boot 4 backend and a Next.js 16 frontend. Simulates real-world ATM/banking operations: account management, deposits, withdrawals, and IBAN-based transfers.

## Architecture

```
clean-bank/                      clean-bank-ui/
├── domain/         (Aggregates, Value Objects, Events)     ├── src/app/        (Next.js App Router)
├── application/    (CQRS Handlers, Mediator)               ├── src/components/ (Reusable UI Components)
├── infrastructure/ (JPA, Flyway, RabbitMQ, Outbox)         ├── src/contexts/   (Auth Context)
└── api/            (REST Controllers, Security)            └── src/lib/        (Axios Client, Utilities)
```

**Dependency direction is strictly inward:**

```
API → Application → Domain ← Infrastructure
```

The domain layer has zero framework dependencies. Infrastructure implements domain interfaces through adapters (Ports & Adapters pattern).

## Tech Stack

### Backend
| Technology | Purpose |
|---|---|
| Java 21, Spring Boot 4 | Runtime & framework |
| Spring Security + JWT (jjwt) | Authentication & authorization |
| Spring Data JPA + PostgreSQL | Persistence |
| Flyway | Database migrations |
| RabbitMQ | Async event processing |
| Springdoc OpenAPI | API documentation (Swagger UI) |

### Frontend
| Technology | Purpose |
|---|---|
| Next.js 16 (App Router) | React framework |
| React 19 | UI library |
| TypeScript 5 | Type safety |
| Tailwind CSS 4 | Styling |
| Axios | HTTP client with interceptors |

## Key Design Patterns

- **CQRS** - Separate command and query models with a custom Mediator that auto-discovers handlers via Spring DI
- **Result Pattern** - Domain operations return `Result<T>` instead of throwing exceptions for business rule violations
- **Aggregate Roots** - `Account`, `Customer`, `Transaction` aggregates with domain event registration
- **Repository Split** - Separate `IAccountWriteRepository` / `IAccountReadRepository` interfaces per aggregate
- **Outbox Pattern** - Domain events persisted transactionally, relayed to RabbitMQ asynchronously
- **Optimistic Locking** - `@Version` on all entities with 409 Conflict handling for concurrent modifications
- **Ports & Adapters** - Domain defines interfaces, infrastructure implements them

## Features

### Banking Operations
- Customer registration with automatic checking account creation
- JWT-based authentication (login/register)
- Multi-currency accounts (TRY, USD, EUR) - Checking, Savings, Investment
- Deposit & withdrawal operations
- IBAN-based money transfers with self-transfer prevention
- Account lifecycle management (activate, freeze, close)
- Paginated transaction history with date range filtering
- Transaction statistics (deposits, withdrawals, transfers in/out, net flow)

### Frontend
- Dark-themed glass-morphism UI
- Reusable component library (Button, Input, Modal, Badge, Pagination, etc.)
- Domain-specific components (AccountCard, AccountActionModal, TransactionTable)
- Toast notifications via react-hot-toast
- Responsive design with mobile support
- Protected routes with JWT token management

## Project Structure

```
clean-bank/
├── domain/          47 classes  - Aggregates, Value Objects, Domain Events, Repository Interfaces
├── application/     47 classes  - Command/Query Handlers, Mediator
├── infrastructure/  36 classes  - JPA Adapters, Entity Mappers, Flyway, RabbitMQ, Outbox
└── api/             19 classes  - REST Controllers, Security Config, Exception Handling

clean-bank-ui/
└── src/             28 files    - Pages, Components, Auth Context, API Client
```

## Getting Started

### Prerequisites
- Java 21+
- Node.js 18+
- PostgreSQL (running on `localhost:5432`, database: `cleanbank`, user/password: `postgres/postgres`)
- RabbitMQ (optional, for async event processing)

### Backend
```bash
cd clean-bank
mvn clean install
mvn spring-boot:run -pl api
```
API runs at `http://localhost:8080` | Swagger UI at `http://localhost:8080/swagger-ui`

### Frontend
```bash
cd clean-bank-ui
npm install
npm run dev
```
UI runs at `http://localhost:3000`

## API Overview

| Endpoint | Method | Description |
|---|---|---|
| `/api/auth/register` | POST | Register new customer |
| `/api/auth/login` | POST | Login, receive JWT |
| `/api/accounts` | POST | Open new account |
| `/api/accounts` | GET | List user's accounts |
| `/api/accounts/{id}/deposit` | POST | Deposit funds |
| `/api/accounts/{id}/withdraw` | POST | Withdraw funds |
| `/api/accounts/{id}/transfer` | POST | Transfer by IBAN |
| `/api/accounts/{id}/transactions` | GET | Paginated transaction history |
| `/api/accounts/{id}/transactions/stats` | GET | Transaction statistics |
| `/api/accounts/{id}/freeze` | PATCH | Freeze account |
| `/api/accounts/{id}/activate` | PATCH | Activate account |
| `/api/customers/{id}` | GET | Customer profile |
| `/api/customers/{id}/personal-info` | PUT | Update personal info |
| `/api/customers/{id}/address` | PUT | Update address |

## Database Migrations

| Version | Description |
|---|---|
| V1 | Initial schema (accounts, customers, transactions, outbox_events) |
| V2 | Users table for authentication |
| V3 | Optimistic locking version columns |
