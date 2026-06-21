# Smart Settlement Demo

Smart Settlement Demo is a Java 17 / Spring Boot 3 backend project that models a simplified invoice settlement platform. It is designed as a Version 0.1 portfolio project for Java backend engineering roles, with a focus on clean service boundaries, business rules, persistence, validation, API design, and testable domain logic.

## Project Overview

The application supports a complete settlement flow:

- create merchants and customers
- issue invoices
- create one settlement case per invoice
- enforce valid settlement status transitions
- simulate payment outcomes
- record settlement history and audit logs

The project is intentionally kept as a modular monolith. It demonstrates production-oriented backend fundamentals without adding unnecessary infrastructure for an early version.

## Business Context

In payment and finance systems, invoice settlement requires traceability, reliable state transitions, and clear audit records. A settlement case should move through controlled lifecycle states, and every meaningful action should be visible for operational review.

This demo captures those concerns in a compact backend:

- invoices are issued with monetary values
- settlements are created and approved
- payment processing is simulated
- settlement state changes are validated
- history and audit records provide traceability

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL
- Flyway
- Bean Validation
- Spring Boot Actuator
- springdoc-openapi / Swagger UI
- Maven
- JUnit 5

## Architecture Overview

The application uses package-by-business-capability rather than layer-based packaging. Each business module owns its controller, DTOs, service, repository, and entity where applicable.

Key architecture choices:

- Modular monolith for simple local development and clear business boundaries
- DTO-only controller contracts
- Business logic in services and domain validators
- Repositories used by services, not controllers
- Flyway-managed schema migrations
- Hibernate schema validation with `ddl-auto: validate`
- `BigDecimal` for all monetary values
- Centralized JSON error responses through a global exception handler

This version deliberately avoids Angular, Kafka, Kubernetes, Spring Security, and microservice decomposition.

## Package Structure

```text
com.yuangang.settlement
├── audit        # Audit log entity, repository, service, API
├── common       # Business exception and global error handling
├── config       # OpenAPI configuration
├── customer     # Customer management
├── invoice      # Invoice creation and issued status
├── merchant     # Merchant management
├── payment      # Payment simulation
└── settlement   # Settlement case lifecycle and status history
```

## Business Workflow

1. A merchant is created.
2. A customer is created.
3. An invoice is created and automatically receives status `ISSUED`.
4. A settlement case is created for the invoice.
5. The settlement moves through valid lifecycle states only:
   - `CREATED -> APPROVED`
   - `CREATED -> REJECTED`
   - `APPROVED -> PAYMENT_PENDING`
   - `PAYMENT_PENDING -> PAID`
   - `PAYMENT_PENDING -> FAILED`
6. Invalid settlement transitions throw a business exception.
7. Every settlement status change creates a history record.
8. Key business actions create audit log records.

## API Overview

Main endpoints:

| Area | Endpoint | Purpose |
| --- | --- | --- |
| Merchants | `POST /api/merchants` | Create merchant |
| Merchants | `GET /api/merchants` | List merchants |
| Customers | `POST /api/customers` | Create customer |
| Customers | `GET /api/customers` | List customers |
| Invoices | `POST /api/invoices` | Create invoice |
| Invoices | `GET /api/invoices` | List invoices |
| Invoices | `GET /api/invoices/{id}` | Get invoice |
| Settlements | `POST /api/settlements` | Create settlement case |
| Settlements | `PATCH /api/settlements/{id}/status` | Update settlement status |
| Settlements | `GET /api/settlements/{id}/history` | View status history |
| Payments | `POST /api/payments/simulations` | Simulate payment result |
| Audit | `GET /api/audit-logs` | View audit logs |

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

Full curl examples are available in [docs/api-examples.md](docs/api-examples.md).

## How To Run Locally

Start PostgreSQL:

```bash
docker compose up -d
```

Database defaults:

```text
Database: smart_settlement
Username: smart_settlement
Password: smart_settlement
Port:     5432
```

Start the application:

```bash
./mvnw spring-boot:run
```

Health check:

```text
http://localhost:8080/actuator/health
```

Flyway applies the schema from:

```text
src/main/resources/db/migration/V1__create_settlement_schema.sql
```

## How To Run Tests

```bash
./mvnw test
```

The test suite includes complete coverage for settlement status transition rules, including all valid and invalid combinations.

## What This Project Demonstrates

- Java and Spring Boot backend application structure
- REST API design with DTO request and response models
- Business-rule enforcement in service/domain code
- Controlled state transitions for a business workflow
- JPA entity modeling and repository usage
- PostgreSQL schema management with Flyway
- Validation with Bean Validation annotations
- Consistent API error responses
- Audit logging and history tracking
- Focused unit testing for domain behavior
- Pragmatic modular monolith design suitable for an early product version

## Roadmap

Potential future versions:

- Add integration tests with PostgreSQL/Testcontainers
- Add pagination and filtering for list endpoints
- Add idempotency keys for invoice and settlement creation
- Add richer payment simulation scenarios
- Add optimistic locking for settlement updates
- Add authentication and authorization with Spring Security
- Add role-based access for operations and audit views
- Add asynchronous payment events with Kafka
- Add observability improvements such as structured logging and metrics
- Add an Angular frontend for demo workflows
- Add containerized deployment configuration
