# Customer & Entity Management Service

A **Core Banking System (CBS) Customer and Entity Management** microservice built with Spring Boot 3 and PostgreSQL. It provides REST APIs to onboard and manage customers (individuals and corporate contacts), legal entities (companies, LLPs, trusts, etc.), their sub-resources (contacts, documents, financials, compliance), and the many-to-many relationship linking customers to entities with roles and ownership data.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [API Reference](#api-reference)
  - [1. Customer Master](#1-customer-master)
  - [2. Customer Contacts](#2-customer-contacts)
  - [3. Customer Documents](#3-customer-documents)
  - [4. Customer Risk](#4-customer-risk)
  - [5. Entity Master](#5-entity-master)
  - [6. Entity Addresses](#6-entity-addresses)
  - [7. Entity Documents](#7-entity-documents)
  - [8. Entity Financials](#8-entity-financials)
  - [9. Entity Compliance](#9-entity-compliance)
  - [10. Customer-Entity Links](#10-customer-entity-links)
- [Swagger UI](#swagger-ui)
- [Postman Collection](#postman-collection)
- [Response Format](#response-format)
- [Error Handling](#error-handling)
- [Configuration](#configuration)

---

## Overview

In corporate banking, **Customer** and **Entity** are distinct but linked concepts:

| Concept | Description |
|---|---|
| **Customer** | The relationship holder — the legal party you KYC. Can be INDIVIDUAL, SOLE_TRADER, or CORPORATE_CONTACT. |
| **Entity** | A legal structure the customer owns or operates through (company, LLP, trust, etc.). Subject to KYB. |
| **Link** | Many-to-many join capturing role type, ownership %, UBO status, signatory rights, and directorship per customer-entity pair. |

Accounts sit under entities, not directly under customers.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 (LTS) |
| Framework | Spring Boot 3.2.4 |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL 18 |
| Migrations | Flyway 10.10.0 |
| Validation | Jakarta Bean Validation |
| Boilerplate | Lombok 1.18.36 |
| API Docs | SpringDoc OpenAPI 2.4.0 (Swagger UI) |
| Build | Apache Maven 3.9.x |

---

## Prerequisites

- **Java 21** — [Download Temurin 21](https://adoptium.net/)
- **Maven 3.9+** — [Download Maven](https://maven.apache.org/download.cgi)
- **PostgreSQL 18** — running locally on port `5432`

---

## Getting Started

### 1. Create the database

```sql
CREATE DATABASE customer_entity;
```

### 2. Configure credentials

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/customer_entity
    username: postgres
    password: your_password
```

### 3. Run the application

```bash
export JAVA_HOME=/path/to/jdk-21
mvn spring-boot:run
```

The app starts on **port 8081**. Flyway applies all 10 migrations automatically.

### 4. Verify

```bash
curl http://localhost:8081/v1/customers
# → {"success":true,"data":[]}

curl http://localhost:8081/v1/entities
# → {"success":true,"data":[]}
```

---

## Project Structure

```
customer-entity/
├── pom.xml
└── src/main/
    ├── java/com/banking/cbs/customer/
    │   ├── CustomerEntityApplication.java
    │   ├── common/
    │   │   ├── config/OpenApiConfig.java
    │   │   ├── exception/CbsException.java
    │   │   ├── exception/GlobalExceptionHandler.java
    │   │   └── response/ApiResponse.java
    │   ├── customer/                        # Customer module
    │   │   ├── controller/CustomerController.java
    │   │   ├── dto/                         # Request + Response DTOs
    │   │   ├── entity/                      # JPA entities
    │   │   ├── repository/                  # Spring Data repos
    │   │   └── service/CustomerService.java
    │   └── entity/                          # Entity module
    │       ├── controller/EntityController.java
    │       ├── dto/
    │       ├── entity/
    │       ├── repository/
    │       └── service/EntityService.java
    └── resources/
        ├── application.yml
        └── db/migration/
            ├── V1__customer_master.sql
            ├── V2__customer_contact.sql
            ├── V3__customer_document.sql
            ├── V4__customer_risk.sql
            ├── V5__entity_master.sql
            ├── V6__entity_address.sql
            ├── V7__entity_document.sql
            ├── V8__entity_financials.sql
            ├── V9__entity_compliance.sql
            └── V10__customer_entity_link.sql
```

---

## Database Schema

10 tables across 10 Flyway migrations:

| Table | Migration | Description |
|---|---|---|
| `customer_master` | V1 | Core customer record (individual or corporate contact) |
| `customer_contact` | V2 | Addresses, phones, emails per customer |
| `customer_document` | V3 | KYC documents (passport, national ID, etc.) |
| `customer_risk` | V4 | AML rating, PEP status, FATCA/CRS, sanctions |
| `entity_master` | V5 | Legal entity (company, LLP, trust, etc.) |
| `entity_address` | V6 | Registered, operating, mailing addresses |
| `entity_document` | V7 | KYB documents (incorporation cert, MOA, etc.) |
| `entity_financials` | V8 | Annual turnover, net worth, credit rating per year |
| `entity_compliance` | V9 | AML, FATCA/CRS, LEI, VAT, sanctions per entity |
| `customer_entity_link` | V10 | M:M join: role, ownership %, UBO, signatory, director flags |

All tables use:
- **UUID primary keys** (`@UuidGenerator`)
- **Optimistic locking** (`@Version`)
- **Audit columns** (`created_by`, `created_at`, `updated_by`, `updated_at`)
- **Soft delete / soft expire** (status field or `effective_to` timestamp)

---

## API Reference

Base URL: `http://localhost:8081`

All responses follow the standard `ApiResponse<T>` envelope.

---

### 1. Customer Master

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/v1/customers` | Onboard a new customer |
| `GET` | `/v1/customers/{customerId}` | Full customer (with contacts, documents, risk) |
| `GET` | `/v1/customers/{customerId}/summary` | Lightweight master fields only |
| `PATCH` | `/v1/customers/{customerId}` | Update mutable fields |
| `PUT` | `/v1/customers/{customerId}/status` | Transition customer status |
| `GET` | `/v1/customers?segment=&rmId=` | List/filter customers |
| `DELETE` | `/v1/customers/{customerId}` | Soft-close (status → CLOSED) |

**Customer types:** `INDIVIDUAL` | `SOLE_TRADER` | `CORPORATE_CONTACT`

**Status transitions:**
- `PENDING_VERIFICATION` → `ACTIVE` (requires kyc_status = VERIFIED)
- `ACTIVE` → `SUSPENDED` | `CLOSED`
- `SUSPENDED` → `ACTIVE` | `CLOSED`

**Immutable fields:** `customerId`, `customerNumber`, `customerType`, `createdBy`

**Onboard request example:**
```json
{
  "customerType": "INDIVIDUAL",
  "title": "Mr",
  "firstName": "James",
  "lastName": "Hargreaves",
  "dateOfBirth": "1978-04-15",
  "nationality": "GBR",
  "countryOfBirth": "GBR",
  "occupation": "Director",
  "employerName": "Acme Corp Ltd",
  "customerSegment": "CORPORATE_CONTACT",
  "onboardingChannel": "BRANCH",
  "onboardingBranch": "LDN-001",
  "relationshipManagerId": "RM-0042",
  "createdBy": "admin"
}
```

---

### 2. Customer Contacts

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/v1/customers/{customerId}/contacts` | Add contact |
| `GET` | `/v1/customers/{customerId}/contacts` | List active contacts |
| `PATCH` | `/v1/customers/{customerId}/contacts/{contactId}` | Update contact |
| `DELETE` | `/v1/customers/{customerId}/contacts/{contactId}` | Soft-expire contact |

**Contact types:** `RESIDENTIAL` | `MAILING` | `WORK` | `CORRESPONDENCE`

If `isPrimary: true`, the existing primary contact of the same type is auto-expired.

---

### 3. Customer Documents

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/v1/customers/{customerId}/documents` | Register KYC document |
| `GET` | `/v1/customers/{customerId}/documents` | List all documents |
| `PATCH` | `/v1/customers/{customerId}/documents/{docId}` | Update document (expiry, storage ref) |
| `DELETE` | `/v1/customers/{customerId}/documents/{docId}` | Revoke document |

**Document types:** `PASSPORT` | `NATIONAL_ID` | `DRIVING_LICENCE` | `UTILITY_BILL` | `BANK_STATEMENT` | `OTHER`

**Document statuses:** `PENDING_VERIFICATION` → `VERIFIED` | `REJECTED` | `REVOKED` | `EXPIRED`

---

### 4. Customer Risk

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/v1/customers/{customerId}/risk` | Get risk profile |
| `PUT` | `/v1/customers/{customerId}/risk` | Replace full risk profile |
| `PATCH` | `/v1/customers/{customerId}/risk` | Partial update (e.g. next_review_date) |

**AML risk ratings:** `LOW` | `MEDIUM` | `HIGH` | `VERY_HIGH`

**PEP statuses:** `NOT_PEP` | `PEP` | `RELATED_TO_PEP`

**FATCA statuses:** `US_PERSON` | `NON_US_PERSON` | `EXEMPT`

---

### 5. Entity Master

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/v1/entities` | Onboard a new entity |
| `GET` | `/v1/entities/{entityId}` | Full entity (with all sub-resources) |
| `GET` | `/v1/entities/{entityId}/summary` | Lightweight master fields |
| `PATCH` | `/v1/entities/{entityId}` | Update mutable fields |
| `PUT` | `/v1/entities/{entityId}/status` | Transition entity status |
| `GET` | `/v1/entities?status=&entityType=&registrationCountry=&rmId=` | List/filter entities |
| `GET` | `/v1/entities/{entityId}/group` | Walk parent chain (group structure) |
| `DELETE` | `/v1/entities/{entityId}` | Soft-close entity |

**Entity types:** `PRIVATE_LIMITED` | `PUBLIC_LIMITED` | `LLP` | `PARTNERSHIP` | `SOLE_TRADER` | `TRUST` | `SPV` | `NGO` | `GOVERNMENT` | `BRANCH` | `COOPERATIVE`

**Legal form validation:**
| Entity Type | Required Legal Form |
|---|---|
| `PRIVATE_LIMITED` | `LTD` |
| `PUBLIC_LIMITED` | `PLC` |
| `LLP` | `LLP` |
| Others | Any |

**Status transitions:**
- `PENDING_VERIFICATION` → `ACTIVE` (requires kyb_status = VERIFIED) | `FAILED`
- `ACTIVE` → `SUSPENDED` | `DORMANT` | `CLOSED`
- `SUSPENDED` → `ACTIVE` | `CLOSED`
- `DORMANT` → `ACTIVE` | `CLOSED`
- `CLOSED` / `BLACKLISTED` → terminal (no exit)

**Immutable fields:** `entityId`, `entityNumber`, `entityType`, `legalForm`, `registrationNumber`, `registrationCountry`

**Group structure:** `parentEntityId` and `ultimateParentId` support multi-level corporate group traversal (max 10 levels).

**Onboard request example:**
```json
{
  "entityName": "Acme Corp Ltd",
  "shortName": "ACME",
  "entityType": "PRIVATE_LIMITED",
  "legalForm": "LTD",
  "registrationNumber": "12345678",
  "registrationCountry": "GBR",
  "registrationDate": "2001-06-15",
  "incorporationDate": "2001-06-15",
  "industryCode": "MANUFACTURING",
  "sicCode": "28110",
  "onboardingBranch": "LDN-001",
  "relationshipManagerId": "RM-0042",
  "createdBy": "admin"
}
```

---

### 6. Entity Addresses

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/v1/entities/{entityId}/addresses` | Add address |
| `GET` | `/v1/entities/{entityId}/addresses` | List active addresses |
| `PATCH` | `/v1/entities/{entityId}/addresses/{addressId}` | Update address |
| `DELETE` | `/v1/entities/{entityId}/addresses/{addressId}` | Soft-expire address |

**Address types:** `REGISTERED` | `OPERATING` | `MAILING` | `BILLING` | `CORRESPONDENCE`

If `isPrimary: true`, the existing primary of the same type is auto-expired.

---

### 7. Entity Documents

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/v1/entities/{entityId}/documents` | Register KYB document |
| `GET` | `/v1/entities/{entityId}/documents` | List documents |
| `PATCH` | `/v1/entities/{entityId}/documents/{docId}` | Update metadata |
| `PUT` | `/v1/entities/{entityId}/documents/{docId}/verify` | Mark document as verified |
| `PUT` | `/v1/entities/{entityId}/documents/{docId}/reject` | Reject document |
| `DELETE` | `/v1/entities/{entityId}/documents/{docId}` | Revoke document |

**Document types:** `CERTIFICATE_OF_INCORPORATION` | `MEMORANDUM_OF_ASSOCIATION` | `ARTICLES_OF_ASSOCIATION` | `REGISTER_OF_DIRECTORS` | `REGISTER_OF_SHAREHOLDERS` | `ANNUAL_RETURN` | `AUDITED_ACCOUNTS` | `VAT_CERTIFICATE` | `TRUST_DEED` | `PARTNERSHIP_AGREEMENT` | `BOARD_RESOLUTION` | `OTHER`

---

### 8. Entity Financials

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/v1/entities/{entityId}/financials` | Get latest financials |
| `PUT` | `/v1/entities/{entityId}/financials` | Replace / upsert financial snapshot |
| `PATCH` | `/v1/entities/{entityId}/financials` | Partial update |

Financials are stored per financial year. `PUT` upserts by `(entityId, financialYear)`.

**Request example:**
```json
{
  "financialYear": "2025",
  "annualTurnover": 24500000,
  "turnoverCurrency": "GBP",
  "netWorth": 8200000,
  "employeeCount": 187,
  "auditorName": "Grant Thornton UK LLP",
  "accountsFiledDate": "2025-09-30",
  "createdBy": "admin"
}
```

---

### 9. Entity Compliance

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/v1/entities/{entityId}/compliance` | Get compliance profile |
| `PUT` | `/v1/entities/{entityId}/compliance` | Replace full compliance record |
| `PATCH` | `/v1/entities/{entityId}/compliance` | Partial update |

**FATCA classifications:** `ACTIVE_NFFE` | `PASSIVE_NFFE` | `FFI` | `EXEMPT`

**CRS classifications:** `ACTIVE_NFFE` | `PASSIVE_NFFE` | `REPORTING_FI` | `NON_REPORTING_FI`

---

### 10. Customer-Entity Links

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/v1/entities/{entityId}/customers` | Link a customer to entity |
| `GET` | `/v1/entities/{entityId}/customers` | List all active links |
| `PATCH` | `/v1/entities/{entityId}/customers/{customerId}` | Update role / ownership % |
| `DELETE` | `/v1/entities/{entityId}/customers/{customerId}` | Unlink (soft-expires the link) |

**Role types:** `DIRECTOR` | `SHAREHOLDER` | `PARTNER` | `TRUSTEE` | `BENEFICIARY` | `AUTHORISED_SIGNATORY` | `UBO` | `OTHER`

**Link flags per row:** `isUbo`, `isAuthorisedSignatory`, `isDirector`, `isPrimaryContact`, `ownershipPercentage`

One customer can hold multiple roles simultaneously on the same entity — all captured as boolean flags on a single link row.

**Link request example:**
```json
{
  "customerId": "17eb2243-ece5-4be8-a0e7-0f90ef1ff3e9",
  "roleType": "DIRECTOR",
  "ownershipPercentage": 55.0,
  "isUbo": true,
  "isAuthorisedSignatory": true,
  "isDirector": true,
  "isPrimaryContact": true,
  "effectiveFrom": "2026-04-05",
  "createdBy": "admin"
}
```

---

## Swagger UI

```
http://localhost:8081/swagger-ui.html
```

Raw OpenAPI JSON spec:

```
http://localhost:8081/v3/api-docs
```

---

## Postman Collection

```
customer-entity.postman_collection.json
```

**How to import:**
1. Open Postman → **Import** → select `customer-entity.postman_collection.json`
2. Run requests **top to bottom** — each POST auto-saves IDs for subsequent requests

**Auto-saved variables:** `customerId`, `contactId`, `docId`, `entityId`, `addressId`, `entityDocId`, `linkId`

**10 folders, 40 requests** covering the full API surface with realistic James Hargreaves / Acme Corp Ltd payloads.

---

## Response Format

All endpoints return a consistent `ApiResponse<T>` envelope:

```json
{
  "success": true,
  "message": "Customer onboarded successfully",
  "data": { },
  "errorCode": null,
  "timestamp": "2026-04-05T10:00:00Z"
}
```

**Error response:**
```json
{
  "success": false,
  "message": "Entity type PRIVATE_LIMITED requires legal form LTD",
  "errorCode": "INVALID_LEGAL_FORM",
  "timestamp": "2026-04-05T10:00:00Z"
}
```

---

## Error Handling

| HTTP Status | Error Code | Cause |
|---|---|---|
| `400` | `INVALID_REQUEST` | Validation failure |
| `400` | `INVALID_LEGAL_FORM` | Entity type / legal form mismatch |
| `404` | `CUSTOMER_NOT_FOUND` | Customer ID does not exist |
| `404` | `ENTITY_NOT_FOUND` | Entity ID does not exist |
| `409` | `DUPLICATE_REGISTRATION` | Registration number + country already exists |
| `409` | `VERSION_MISMATCH` | Optimistic lock conflict |
| `422` | `INVALID_STATUS_TRANSITION` | Attempted illegal status change |
| `422` | `TERMINAL_STATUS` | Entity/customer is CLOSED or BLACKLISTED |
| `500` | `INTERNAL_ERROR` | Unexpected server error |

---

## Configuration

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/customer_entity
    username: postgres
    password: your_password
  jpa:
    hibernate:
      ddl-auto: validate      # Flyway owns the schema
  flyway:
    enabled: true
    baseline-on-migrate: true

springdoc:
  swagger-ui:
    path: /swagger-ui.html
    try-it-out-enabled: true

logging:
  level:
    com.banking.cbs: DEBUG
```

---

## GitHub Repository

[https://github.com/banksoft2026/customer-entity](https://github.com/banksoft2026/customer-entity)
