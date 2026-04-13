# CleanBank - Core Banking Platform

> 🔗 **Live Demo:** [https://ahmedshn.com](https://ahmedshn.com)
> Demo credentials — Admin: `admin@cleanbank.com` / `admin123`

A full-stack banking application built with **Clean Architecture** principles, featuring a Spring Boot 4 backend and a Next.js 16 frontend. Simulates real-world ATM/banking operations: account management, deposits, withdrawals, IBAN-based transfers, and a real-time audit dashboard — with **role-based access control** (Admin, Employee, Customer).

**Deployed on AWS EC2** with Docker Compose, PostgreSQL, RabbitMQ, and Caddy as a reverse proxy with automatic Let's Encrypt TLS.

## Architecture

```
clean-bank/                      clean-bank-ui/
├── domain/         (Aggregates, Value Objects, Events)     ├── src/app/        (Next.js App Router)
├── application/    (CQRS Handlers, Mediator)               ├── src/components/ (Reusable UI Components)
├── infrastructure/ (JPA, Flyway, RabbitMQ, Outbox)         ├── src/contexts/   (Auth Context)
└── api/            (REST Controllers, Security)            └── src/lib/        (Axios Client, Services)
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
| Spring Security + JWT (jjwt) | Authentication & role-based authorization |
| Spring Data JPA + PostgreSQL | Persistence |
| Flyway | Database migrations |
| RabbitMQ | Async event processing |
| Server-Sent Events (SSE) | Real-time audit event streaming |
| Springdoc OpenAPI | API documentation (Swagger UI) |

### Frontend
| Technology | Purpose |
|---|---|
| Next.js 16 (App Router) | React framework |
| React 19 | UI library |
| TypeScript 5 | Type safety |
| Tailwind CSS 4 | Styling |
| Recharts | Dashboard charts |
| Axios | HTTP client with interceptors |

### Infrastructure & Deployment
| Technology | Purpose |
|---|---|
| Docker / Docker Compose | Containerization and local/prod orchestration |
| AWS EC2 (Ubuntu) | Cloud host |
| Caddy 2 | Reverse proxy with automatic Let's Encrypt TLS |
| Namecheap DNS | Domain / A records |

## Key Design Patterns

- **CQRS** - Separate command and query models with a custom Mediator that auto-discovers handlers via Spring DI
- **Result Pattern** - Domain operations return `Result<T>` instead of throwing exceptions for business rule violations
- **Aggregate Roots** - `Account`, `Customer`, `Transaction` aggregates with domain event registration
- **Repository Split** - Separate `IAccountWriteRepository` / `IAccountReadRepository` interfaces per aggregate
- **Outbox Pattern** - Domain events persisted transactionally in the same DB transaction as the aggregate save, then relayed to RabbitMQ asynchronously by a scheduled processor (at-least-once delivery, no event loss on crash)
- **Real-time Audit Dashboard** - Domain events are persisted as audit records and broadcast to connected admins via Server-Sent Events (SSE) for a live activity feed
- **Optimistic Locking** - `@Version` on all entities with 409 Conflict handling for concurrent modifications
- **Ports & Adapters** - Domain defines interfaces, infrastructure implements them

## Role-Based Access Control

Three roles with distinct capabilities:

| Role | Capabilities | Frontend Dashboard |
|---|---|---|
| **Admin** | View all system transactions (paginated), manage staff users (create Admin/Employee), view and manage all customers | Stats overview, transaction audit, user management, customer management with action buttons |
| **Employee** | Register new customers, freeze/activate/close accounts, suspend/activate/close customers | Quick links to customer registration and ID-based customer/account management panel |
| **Customer** | Open accounts (Checking/Savings/Investment), deposit, withdraw, IBAN transfer, view transaction history, manage profile | Balance overview, account cards, quick actions, profile settings |

- JWT tokens carry `role` and `customerId` claims
- Backend enforces role-based endpoint protection via Spring Security (`hasRole("ADMIN")`, `hasAnyRole("ADMIN", "EMPLOYEE")`)
- Frontend uses `RoleGuard` component for client-side route protection and role-aware sidebar navigation
- A default admin user (`admin@cleanbank.com` / `admin123`) is seeded on startup

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

### Admin & Employee Operations
- Admin: create staff users (Admin/Employee roles)
- Admin: view all users, customers, and system-wide transactions
- Employee: register walk-in customers
- Employee: freeze/activate/close accounts by ID
- Employee: suspend/activate/close customers by ID
- Admin: real-time audit event stream (SSE) with historical filtering by aggregate type and severity

### Frontend
- Dark-themed glass-morphism UI
- Role-based dashboards and navigation (Admin / Employee / Customer)
- Reusable component library (Button, Input, Modal, Badge, Pagination, etc.)
- Domain-specific components (AccountCard, AccountActionModal, TransactionTable, AdminTransactionTable)
- Toast notifications via react-hot-toast
- Responsive design with mobile support
- Protected routes with JWT token management and RoleGuard

## Getting Started

### Option A — Docker Compose (recommended, one command)

Requires Docker and Docker Compose.

```bash
cp .env.example .env
# edit .env: set JWT_SECRET (openssl rand -hex 32), DB/RabbitMQ passwords, DOMAIN, etc.
docker compose up -d --build
```

This brings up PostgreSQL, RabbitMQ, the Spring Boot backend, the Next.js frontend, and Caddy (reverse proxy + automatic TLS) behind a single domain.

### Option B — Local development (without Docker)

**Prerequisites:** Java 21+, Node.js 18+, PostgreSQL running on `localhost:5432` (db `cleanbank`, user/password `postgres/postgres`), RabbitMQ (optional).

```bash
# Backend
cd clean-bank
mvn clean install
mvn spring-boot:run -pl api
# → http://localhost:8080 | Swagger UI: http://localhost:8080/swagger-ui

# Frontend (in another terminal)
cd clean-bank-ui
npm install
npm run dev
# → http://localhost:3000
```

### Default Admin Account
On first startup, the backend seeds an admin user:
- **Email:** `admin@cleanbank.com`
- **Password:** `admin123`

## API Overview

### Public
| Endpoint | Method | Description |
|---|---|---|
| `/api/auth/register` | POST | Register new customer |
| `/api/auth/login` | POST | Login, receive JWT |

### Customer (authenticated + ownership)
| Endpoint | Method | Description |
|---|---|---|
| `/api/accounts` | POST | Open new account |
| `/api/accounts` | GET | List user's accounts |
| `/api/accounts/{id}/deposit` | POST | Deposit funds |
| `/api/accounts/{id}/withdraw` | POST | Withdraw funds |
| `/api/accounts/{id}/transfer` | POST | Transfer by IBAN |
| `/api/accounts/{id}/transactions` | GET | Paginated transaction history |
| `/api/accounts/{id}/transactions/stats` | GET | Transaction statistics |
| `/api/customers/{id}` | GET | Customer profile |
| `/api/customers/{id}/personal-info` | PUT | Update personal info |
| `/api/customers/{id}/address` | PUT | Update address |

### Employee (Admin or Employee role)
| Endpoint | Method | Description |
|---|---|---|
| `/api/employees/customers` | POST | Register customer on behalf |
| `/api/employees/accounts/{id}/freeze` | PATCH | Freeze account |
| `/api/employees/accounts/{id}/activate` | PATCH | Activate account |
| `/api/employees/accounts/{id}/close` | PATCH | Close account |
| `/api/employees/customers/{id}/suspend` | PATCH | Suspend customer |
| `/api/employees/customers/{id}/activate` | PATCH | Activate customer |
| `/api/employees/customers/{id}/close` | PATCH | Close customer |

### Admin only
| Endpoint | Method | Description |
|---|---|---|
| `/api/admin/users` | POST | Create staff user (Admin/Employee) |
| `/api/admin/users` | GET | List all users |
| `/api/admin/customers` | GET | List all customers |
| `/api/admin/transactions` | GET | All transactions (paginated) |
| `/api/admin/events` | GET | Audit events (paginated, filterable by aggregate type / severity) |
| `/api/admin/events/stream` | GET | Real-time audit event stream (SSE) |

## Database Migrations

| Version | Description |
|---|---|
| V1 | Initial schema (accounts, customers, transactions, outbox_events) |
| V2 | Users table for authentication |
| V3 | Optimistic locking version columns |
| V4 | Audit events table |

## Deployment

The live demo runs on a single AWS EC2 instance using the `docker-compose.yml` at the repo root. The stack:

```
          ┌──────────────────────── AWS EC2 (Ubuntu) ────────────────────────┐
Internet ─┤ :80 / :443 → Caddy ─┬─ /api/*, /swagger-ui/*  →  backend:8080    │
          │                     └─ /*                     →  frontend:3000  │
          │                                                                  │
          │            backend ──▶ postgres:5432   (Flyway-managed schema)   │
          │            backend ──▶ rabbitmq:5672   (outbox → queues)         │
          └──────────────────────────────────────────────────────────────────┘
```

- Caddy auto-provisions Let's Encrypt certificates on first start — no manual TLS setup
- All secrets (DB password, JWT secret, RabbitMQ credentials) live in a gitignored `.env` file; see `.env.example` for the template
- `restart: unless-stopped` on every service means the stack survives reboots
- PostgreSQL and RabbitMQ state is persisted to named Docker volumes (`pgdata`, `rabbitdata`); Caddy's ACME state is persisted to `caddydata` so certificates survive container rebuilds
