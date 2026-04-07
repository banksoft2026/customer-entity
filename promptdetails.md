# customer-entity — Prompt History & Development Log

This service handles Customer and Entity onboarding, management, and KYC for the BankSoft CBS platform.

- **Port:** 8081
- **Base URL:** `http://localhost:8081`
- **Database:** PostgreSQL
- **Framework:** Spring Boot 3 / Java 21

---

## Prompt 1 — Initial Service Build

**Prompt:**
> "Build the customer-entity microservice with full customer and entity management APIs."

### Steps Taken
1. Created Spring Boot project with dependencies: Spring Web, Spring Data JPA, PostgreSQL driver, Lombok, Validation, Actuator, SpringDoc OpenAPI
2. Defined domain entities:
   - `CustomerMaster` — individual customer records
   - `CustomerContact` — address, phone, email
   - `CustomerDocument` — KYC identity documents
   - `EntityMaster` — corporate/legal entity records
   - `EntityAddress` — registered address
   - `EntityFinancials` — annual accounts, turnover, net worth
   - `EntityCompliance` — AML, FATCA, CRS, LEI, PEP
   - `EntityDocument` — entity KYC documents
   - `CustomerEntityLink` — links customers to entities
3. Created DTOs: `CustomerRequest`, `CustomerResponse`, `EntityRequest`, `EntityResponse` and all sub-DTOs
4. Created repositories, services, and controllers for all entities
5. Configured `application.yml` with datasource, JPA settings, server port 8081
6. Added `GlobalExceptionHandler` for standardised `ApiResponse` error wrapping
7. Added Postman collection `customer-entity.postman_collection.json`

### Key API Endpoints
| Method | Path | Description |
|--------|------|-------------|
| POST | `/v1/customers` | Create new customer |
| GET | `/v1/customers` | List customers (paginated) |
| GET | `/v1/customers/{id}` | Get customer by ID |
| PATCH | `/v1/customers/{id}/status` | Update customer status |
| POST | `/v1/customers/{id}/contacts` | Add contact details |
| POST | `/v1/customers/{id}/documents` | Add identity document |
| POST | `/v1/entities` | Create new entity |
| GET | `/v1/entities` | List entities (paginated) |
| GET | `/v1/entities/{id}` | Get entity by ID |
| POST | `/v1/entities/{id}/address` | Add entity address |
| POST | `/v1/entities/{id}/financials` | Add financial data |
| POST | `/v1/entities/{id}/compliance` | Add compliance data |
| POST | `/v1/entities/{id}/documents` | Add entity document |
| POST | `/v1/customers/{customerId}/entities/{entityId}` | Link customer to entity |

---

## Prompt 2 — README Documentation

**Prompt:**
> "Prep readme.md files in git for user-admin and bank ops in respective repos."

### Steps Taken
1. Created `README.md` with:
   - Service overview and purpose
   - Prerequisites (Java 21, PostgreSQL, Maven)
   - Setup and run instructions
   - API endpoint summary
   - Environment configuration reference
2. Committed and pushed to GitHub (`banksoft2026/customer-entity`)

---

## Prompt 3 — Fix CORS Errors

**Prompt:**
> "Errors on UI — CORS policy: No 'Access-Control-Allow-Origin' header on http://localhost:8081"

### Root Cause
The service had no CORS configuration. Browser preflight (`OPTIONS`) requests to `http://localhost:8081` from the frontend running on `http://localhost:5173` were blocked because the response contained no `Access-Control-Allow-Origin` header.

### Steps Taken
1. Created `src/main/java/com/banking/cbs/customer/common/config/WebConfig.java`
2. Implemented `WebMvcConfigurer.addCorsMappings()` allowing:
   - All origin patterns (`*`)
   - Methods: `GET, POST, PUT, PATCH, DELETE, OPTIONS`
   - All headers
   - Credentials: `true`
   - Max age: 3600 seconds
3. Committed and pushed to GitHub

### File Added
```
src/main/java/com/banking/cbs/customer/common/config/WebConfig.java
```

### Verification
```bash
curl -I -X OPTIONS http://localhost:8081/v1/customers \
  -H "Origin: http://localhost:5173" \
  -H "Access-Control-Request-Method: GET"
# Returns: Access-Control-Allow-Origin: http://localhost:5173
```

### Issues Resolved
| Issue | Fix |
|-------|-----|
| CORS blocked for all endpoints | Added `WebConfig.java` implementing `WebMvcConfigurer` |

---

## Prompt 4 — Service Restart

**Prompt:**
> "Restart all services after CORS fix."

### Steps Taken
1. Existing process on port 8081 was killed
2. `git pull` — already up to date
3. Attempted `./mvnw spring-boot:run` — **failed** (no Maven wrapper)
4. Located Maven: `/Users/shubharthibhattacharya/tools/apache-maven-3.9.6/bin/mvn`
5. Attempted with Java 25 — **failed** (`TypeTag :: UNKNOWN` compiler error)
6. Set `JAVA_HOME` to Java 21 (`jdk-21.0.6+7`)
7. Started with `mvn spring-boot:run` using Java 21 — **success**
8. Service confirmed UP on port 8081

### Issues Resolved
| Issue | Fix |
|-------|-----|
| No `mvnw` file | Used full Maven path |
| Java 25 incompatible with `maven-compiler-plugin:3.11.0` | Set `JAVA_HOME` to Java 21 |

---

## Key DTOs

### CustomerRequest
| Field | Required | Notes |
|-------|----------|-------|
| customerType | ✓ | INDIVIDUAL / CORPORATE |
| firstName | ✓ | |
| lastName | ✓ | |
| fullName | ✓ | |
| dateOfBirth | ✓ | |
| nationality | ✓ | ISO 3166-1 alpha-3 |
| createdBy | ✓ | |
| title | | Mr/Mrs/Ms/Dr/Prof |
| gender | | MALE/FEMALE/OTHER |
| maritalStatus | | |
| occupation | | |
| employerName | | |
| incomeRange | | |
| countryOfBirth | | |
| customerStatus | | ACTIVE/PENDING_VERIFICATION/SUSPENDED/CLOSED |
| kycReviewDate | | |
| onboardingChannel | | BRANCH/ONLINE/MOBILE/AGENT |
| onboardingBranch | | |
| relationshipManagerId | | |
| relationshipSince | | |
| contact | | Nested ContactRequest |
| document | | Nested DocumentRequest |

### EntityRequest
| Field | Required | Notes |
|-------|----------|-------|
| entityType | ✓ | COMPANY/PARTNERSHIP/TRUST/GOVERNMENT/NGO/SOLE_TRADER |
| entityName | ✓ | |
| registrationNumber | ✓ | |
| incorporationCountry | ✓ | |
| createdBy | ✓ | |
| shortName | | |
| registrationDate | | |
| industryCode | | |
| sicCode | | |
| entityStatus | | ACTIVE/PENDING/SUSPENDED/DISSOLVED |
| kybStatus | | PENDING/IN_PROGRESS/APPROVED/REJECTED |
| address | | Nested EntityAddressRequest |
| financials | | Nested EntityFinancialsRequest |
| compliance | | Nested EntityComplianceRequest |
