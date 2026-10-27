# MedCheck-Server

[![Java](https://img.shields.io/badge/Java-21-orange.svg?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen.svg?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-8.0-red.svg?style=flat-square&logo=redis)](https://redis.io/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-4.x-orange.svg?style=flat-square&logo=rabbitmq)](https://www.rabbitmq.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg?style=flat-square&logo=docker)](https://www.docker.com/)
[![Postman](https://img.shields.io/badge/Postman-Test%20Specification-FF6C37.svg?style=flat-square&logo=postman)](https://durodola-abolaji-s-team.postman.co/workspace/Team-Workspace~38bda554-bdc0-443b-8f9a-9a7bd7b74acd/collection/47831221-5f933937-69e3-4a11-abe3-bf195ce83f3e?action=share&source=copy-link&creator=47831221)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](https://opensource.org/licenses/MIT)

---

## Table of Contents
- [Overview](#overview)
- [Core Features](#core-features)
- [Technology Stack](#technology-stack)
- [System Architecture](#system-architecture)
- [Data Model & Hierarchy](#data-model--hierarchy)
- [Environment Configuration](#environment-configuration)
- [Getting Started & Installation](#getting-started--installation)
  - [Option 1: Docker Compose (Recommended)](#option-1-docker-compose-recommended)
  - [Option 2: Local Development Setup](#option-2-local-development-setup)
- [Security & Role-Based Access Control (RBAC)](#security--role-based-access-control-rbac)
- [Postman Test Specification](#postman-test-specification)
- [Comprehensive API Reference](#comprehensive-api-reference)
  - [1. Authentication Endpoints (`/api/v1/auth`)](#1-authentication-endpoints-apiv1auth)
  - [2. Manufacturer Endpoints (`/api/v1/manufacturer`)](#2-manufacturer-endpoints-apiv1manufacturer)
  - [3. Wholesaler Endpoints (`/api/v1/wholesaler`)](#3-wholesaler-endpoints-apiv1wholesaler)
  - [4. Retailer Endpoints (`/api/v1/retailer`)](#4-retailer-endpoints-apiv1retailer)
  - [5. Consumer Endpoints (`/api/v1/consumer`)](#5-consumer-endpoints-apiv1consumer)
  - [6. Verification Endpoints (`/api/v1/verification`)](#6-verification-endpoints-apiv1verification)
  - [7. Administrator Endpoints (`/api/v1/admin`)](#7-administrator-endpoints-apiv1admin)
  - [8. Investigator Endpoints (`/api/v1/investigator`)](#8-investigator-endpoints-apiv1investigator)
  - [9. User Profile Endpoints (`/api/v1/profile`)](#9-user-profile-endpoints-apiv1profile)
  - [10. Health & Diagnostics (`/test`)](#10-health--diagnostics-test)
- [Error Handling](#error-handling)
- [Project Directory Structure](#project-directory-structure)
- [License](#license)

---

## Overview

**MedCheck** is an enterprise-grade pharmaceutical anti-counterfeiting backend system engineered specifically for the Nigerian healthcare and pharmaceutical distribution supply chain. It provides transparent, immutable, and cryptographic chain-of-custody tracking across all pharmaceutical supply chain tiers:

- **Manufacturers**: Register pharmaceutical products with NAFDAC credentials, generate production batches with serialized codes, and initiate distribution.
- **Wholesalers & Distributors**: Accept batches in bulk and route custody down to sub-distributors and retail pharmacies at pack levels.
- **Retail Pharmacies & Dispensaries**: Verify authentic packaging, dispense packs, and manage retail-level custody transfers.
- **Consumers & Patients**: Verify medications before consumption at the pack or sachet level, detect counterfeits, and report suspicious products.
- **Regulatory Investigators (NAFDAC, Law Enforcement)**: Audit custody logs, verify batch histories, investigate counterfeit flags, and suspend compromised entities.

The system utilizes hierarchical cryptographic verification codes across **Batches**, **Packs**, and **Sachets** coupled with verification count auditing to detect duplicate scans and counterfeit replication attempts.

---

## Ecosystem & Repositories

MedCheck operates as a unified multi-platform ecosystem across backend, mobile, and web:

| Platform       | Repository                                                          | Stack                                    | Description                                                                   |
|----------------|---------------------------------------------------------------------|------------------------------------------|-------------------------------------------------------------------------------|
| **Mobile App** | [**MedCheck-Mobile**](https://github.com/TheDurodola/Medify-Mobile) | Flutter, Dart, Clean Architecture & BLoC | Native mobile scanner for on-the-go patient and inspector drug authentication |
| **Web Client** | [**MedCheck-Web**](https://github.com/TheDurodola/MedCheck-Client)  | React 19, Vite 7, Tailwind CSS v4        | Web console for manufacturers, depots, retail pharmacies, and consumers       |

---
## Core Features

- **Hierarchical Supply Chain Serialization**:
  Full 3-level serialization (`Drug` &rarr; `Batch` &rarr; `Pack` &rarr; `Sachet`) ensuring every blister strip or sachet carries a distinct, traceable code linked directly to its parent pack and manufacturing batch.
- **Role-Based Access Control (RBAC)**:
  Granular security enforcing separation of duty across Administrators, Manufacturers, Wholesalers, Retailers, Investigators, and Consumers.
- **Stateless Custom JWT Security Pipeline**:
  High-performance Spring Security filter chain with HMAC256 token verification, account status validation, and customizable session TTL.
- **Real-Time Verification & Counter Audit**:
  Instant authentication of batches, packs, and sachets with monotonic verification counters that flag potential barcode/QR cloning if scanned multiple times across unexpected locations.
- **Immutable Chain-of-Custody Logging**:
  Granular logistics audit trail tracking every transfer between supply chain organizations, including sender, recipient, timestamp, and product tier.
- **Asynchronous Event-Driven Messaging**:
  Decoupled background workflows using RabbitMQ for reliable event publishing and consumption (e.g., welcome emails upon registration).
- **Distributed Caching with Redis**:
  High-speed in-memory caching for batch and pack lookup optimization.
- **Transactional Email Service**:
  Brevo (Sendinblue) API integration with HTML email templating for automated user registration and onboarding alerts.
- **Cloud Media Management**:
  Cloudinary integration for secure uploading, processing, and retrieval of user profile pictures and investigation documentation.
- **Financial Integration**:
  Paystack Payment Gateway client with logging interceptor for automated bank listing, transfer recipient registration, and payment settlement workflows.

---

## Technology Stack

| Category | Technology |
|---|---|
| **Language & Platform** | Java 21 LTS |
| **Framework** | Spring Boot 4.x / Spring Framework 6.x |
| **Security & Auth** | Spring Security 6, Auth0 Java JWT (`HMAC256`) |
| **Primary Database** | PostgreSQL 16 (Spring Data JPA / Hibernate) |
| **Distributed Cache** | Redis 8.0 (Alpine) |
| **Message Broker** | RabbitMQ 4 (AMQP Protocol) with Management UI |
| **Transactional Email** | Brevo (Sendinblue) REST API |
| **Media Storage** | Cloudinary Java SDK |
| **Payment Gateway** | Paystack API (REST Client with Logging Interceptor) |
| **Utilities & Validation** | Hibernate Validator, Google Libphonenumber, Apache Tika |
| **Containerization** | Docker, Docker Compose |
| **Build Tool** | Apache Maven 3.9+ |

---

## System Architecture

```mermaid
flowchart TD
    Client["Client Layer\n(Mobile App / Web UI / Pharmacy Barcode Scanner)"]

    subgraph Security ["Security & Filter Pipeline"]
        AuthFilter["CustomAuthenticationFilter\n(/api/v1/auth/signin)"]
        TokenGen["HMAC256 JWT Token Issuer"]
        AuthzFilter["CustomAuthorizationFilter\n(Bearer Token Validator & Status Check)"]
    end

    subgraph Controllers ["REST API Controllers"]
        AuthController["AuthController"]
        MfrController["ManufacturerController"]
        WhsController["WholesalerController"]
        RetController["RetailerController"]
        ConsController["ConsumerController"]
        VerController["VerificationController"]
        AdmController["AdminController"]
        InvController["InvestigatorController"]
    end

    subgraph Services ["Application & Domain Services"]
        AuthService["AuthServiceImpl"]
        BatchService["BatchServiceImpl"]
        DrugService["DrugServiceImpl"]
        OrgService["OrganisationServiceImpl"]
        ReportService["ReportServiceImpl"]
        EventPub["RabbitMqAuthEventPublisher"]
    end

    subgraph AsyncBus ["Event Bus & Workers"]
        RabbitMQ["RabbitMQ 4.x\n(Exchange: medcheck_exchange)"]
        EventConsumer["RabbitMqAuthEventConsumer"]
    end

    subgraph Infrastructure ["Storage & External Providers"]
        Postgres[("PostgreSQL 16\n(Relational Storage)")]
        Redis[("Redis 8.0\n(Distributed Cache)")]
        Brevo["Brevo API\n(Transactional Emails)"]
        Cloudinary["Cloudinary\n(Profile Media)"]
        Paystack["Paystack API\n(Payment & Transfers)"]
    end

    Client -->|HTTP / JSON| AuthFilter
    AuthFilter --> TokenGen
    Client -->|Authorization: Bearer JWT| AuthzFilter
    AuthzFilter --> Controllers

    Controllers --> Services
    Services --> Postgres
    Services --> Redis
    Services --> EventPub
    Services --> Cloudinary
    Services --> Paystack

    EventPub -->|UserRegisteredEvent| RabbitMQ
    RabbitMQ --> EventConsumer
    EventConsumer --> Brevo
```

---

## Data Model & Hierarchy

The MedCheck domain tracks pharmaceutical items hierarchically from Drug registration to unit-level consumer delivery:

```mermaid
erDiagram
    ORGANISATION ||--o{ USER_ACCOUNT : "employs"
    ORGANISATION ||--o{ DRUG : "manufactures"
    ORGANISATION ||--o{ BATCH_LOGISTICS : "sends/receives"
    ORGANISATION ||--o{ PACK_LOGISTICS : "sends/receives"

    DRUG ||--o{ BATCH : "produced in"
    BATCH ||--o{ PACK : "contains"
    PACK ||--o{ SACHET : "contains"

    BATCH ||--o{ BATCH_LOGISTICS : "tracked by"
    PACK ||--o{ PACK_LOGISTICS : "tracked by"
    PACK ||--o{ REPORT : "reported in"
    REPORT ||--o{ REPORT_ASSIGNMENT : "assigned to"
    USER_ACCOUNT ||--o{ REPORT_ASSIGNMENT : "investigates"

    DRUG {
        string id PK
        string brandName
        string genericName
        string nafdacRegistrationNumber
        string drugCode
        int expirationDurationInDays
    }

    BATCH {
        string id PK
        string batchIdentifier
        string verificationCode UK
        bigint verificationCount
        date manufactureDate
        date expirationDate
    }

    PACK {
        string id PK
        string packIdentifier
        string verificationCode UK
        bigint verificationCount
        boolean isSold
    }

    SACHET {
        string id PK
        string sachetIdentifier
        string verificationCode UK
        bigint verificationCount
        boolean isConsumed
    }

    USER_ACCOUNT {
        string id PK
        string username UK
        string email UK
        string role
        string accountStatus
        string nationalIdentityNumber
    }

    ORGANISATION {
        string id PK
        string name
        string organisationCode UK
        string organizationType
    }
```

---

## Environment Configuration

Configure the required variables by creating a `.env` file in the project root:

```bash
cp .example.env .env
```

### Complete Environment Variable Reference

| Variable | Description | Default / Example |
|---|---|---|
| `DATABASE_URL` | JDBC PostgreSQL connection URL | `jdbc:postgresql://db:5432/medcheck_db` |
| `DATABASE_USERNAME` | PostgreSQL username | `postgres` |
| `DATABASE_PASSWORD` | PostgreSQL password | `your_secure_password` |
| `DATABASE_PORT` | PostgreSQL host binding port | `5432` |
| `SERVER_PORT` | Embedded application server port | `8080` (mapped to `8081` in compose) |
| `JWT_SIGNING_KEY` | Secret key used for signing JWT tokens | `your_jwt_secret_key` |
| `JWT_DURATION_IN_SECONDS` | JWT expiration duration in seconds | `86400` (24 hours) |
| `VERIFICATION_CODE_LENGTH` | Character length for generated verification codes | `12` |
| `REDIS_HOST` | Redis cache hostname | `redis` (or `localhost`) |
| `REDIS_PORT` | Redis cache port | `6379` |
| `TIME_TO_LIVE_IN_HOURS` | Cache TTL for cached verification objects | `24` |
| `RABBITMQ_HOST` | RabbitMQ broker hostname | `rabbitmq` (or `localhost`) |
| `RABBITMQ_PORT` | RabbitMQ AMQP port | `5672` |
| `RABBITMQ_UI_PORT` | RabbitMQ web management console port | `15672` |
| `RABBITMQ_USER` | RabbitMQ broker username | `guest` |
| `RABBITMQ_PASSWORD` | RabbitMQ broker password | `guest` |
| `RABBITMQ_EXCHANGE_NAME` | Topic exchange name | `medcheck_exchange` |
| `RABBITMQ_USER_REGISTERED_ROUTING_KEY` | Routing key for registration events | `user.registered` |
| `RABBITMQ_QUEUE_NAME` | Queue name for registration consumer | `medcheck_queue` |
| `BREVO_API_KEY` | Brevo (Sendinblue) API v3 key | `xkeysib-...` |
| `BREVO_EMAIL_SENDER` | Verified sender address for Brevo emails | `no-reply@medcheck.ng` |
| `CLOUDINARY_URL` | Cloudinary connection string for media storage | `cloudinary://api_key:api_secret@cloud_name` |
| `PAYSTACK_BASE_URL` | Paystack REST API base endpoint | `https://api.paystack.co` |
| `PAYSTACK_API_SECRET_KEY` | Paystack secret key | `sk_test_...` |

---

## Getting Started & Installation

### Option 1: Docker Compose (Recommended)

Run the full system stack (Spring Boot Application, PostgreSQL 16, Redis 8, and RabbitMQ 4) with a single command:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/TheDurodola/MedCheck-Server.git
   cd MedCheck-Server
   ```

2. **Configure environment settings**:
   ```bash
   cp .example.env .env
   # Edit .env with your credentials (e.g. Brevo, Cloudinary, Paystack)
   ```

3. **Build and start all services**:
   ```bash
   docker-compose up --build -d
   ```

4. **Verify container health**:
   ```bash
   docker-compose ps
   ```

5. **Access services**:
   - MedCheck Server API: `http://localhost:8081`
   - Health Probe: `http://localhost:8081/test/live`
   - RabbitMQ Management Console: `http://localhost:15672` (Default: `guest` / `guest`)

---

### Option 2: Local Development Setup

#### Prerequisites
- Java 21 JDK installed (`java -version`)
- Maven 3.9+ (or use the included `./mvnw`)
- Running PostgreSQL 16 instance
- Running Redis instance
- Running RabbitMQ instance

#### Step-by-Step Instructions
1. **Clone the repository and set up environment**:
   ```bash
   git clone https://github.com/TheDurodola/MedCheck-Server.git
   cd MedCheck-Server
   cp .example.env .env
   ```
2. **Build the application**:
   ```bash
   ./mvnw clean package -DskipTests
   ```
3. **Run the Spring Boot application**:
   ```bash
   ./mvnw spring-boot:run
   ```
4. **Access the application**:
   ```bash
   curl http://localhost:8080/test/live
   # Response: "We are fucking live"
   ```

---

## Security & Role-Based Access Control (RBAC)

MedCheck secures every endpoint through custom stateless filter interceptors:
- **`CustomAuthenticationFilter`**: Listens on `POST /api/v1/auth/signin`. Authenticates credentials, generates an HMAC256-signed JWT token, and injects user authority into the response.
- **`CustomAuthorizationFilter`**: Validates the `Authorization: Bearer <token>` header on every incoming request, validates user account status (`ACTIVE` vs `SUSPENDED`), and establishes the `SecurityContext`.

### User Roles & Authorities

| Role | Permitted Actions |
|---|---|
| `ADMINISTRATOR` | System oversight, organization onboarding, user account suspension/unsuspension. |
| `MANUFACTURING_EMPLOYEE` | Drug registration, batch generation with serialized codes, batch transfer to wholesalers. |
| `WHOLESALE_EMPLOYEE` | Batch transfer to wholesalers/retailers, individual pack transfers to retail outlets. |
| `RETAIL_EMPLOYEE` | Pack distribution, retail sales, pack authentication. |
| `INVESTIGATOR` | Regulatory audit, organization & user inspections, suspension of bad actors, full-hierarchy verification. |
| `CONSUMER` | Unit-level verification (packs & sachets), pack consumption registration. |

### Authorization Header Format
```http
Authorization: Bearer <your_jwt_token>
```

---

## Postman Test Specification

The complete interactive **Postman Test Specification & API Collection** for MedCheck Server can be found at:

> [!TIP]
> Access the live Postman Workspace and Collection with pre-configured requests, environment variables, authentication workflows, and automated test assertions:
>
> 🚀 **[MedCheck Postman Test Specification Workspace](https://durodola-abolaji-s-team.postman.co/workspace/Team-Workspace~38bda554-bdc0-443b-8f9a-9a7bd7b74acd/collection/47831221-5f933937-69e3-4a11-abe3-bf195ce83f3e?action=share&source=copy-link&creator=47831221)**
>
> **Direct URL**: `https://durodola-abolaji-s-team.postman.co/workspace/Team-Workspace~38bda554-bdc0-443b-8f9a-9a7bd7b74acd/collection/47831221-5f933937-69e3-4a11-abe3-bf195ce83f3e?action=share&source=copy-link&creator=47831221`

---

## Comprehensive API Reference

### 1. Authentication Endpoints (`/api/v1/auth`)

#### `POST /api/v1/auth/signup`
Registers a new user account across any role in the platform. Automatically assigns roles and binds employees to their respective organizations. Publishes `UserRegisteredEvent` to RabbitMQ for transactional welcome emails.

- **Authorization**: Public (`Permit All`)
- **Request Body (`RegisterUserRequest`)**:
  ```json
  {
    "username": "johndoe",
    "firstName": "John",
    "lastName": "Doe",
    "middleName": "Alexander",
    "password": "Password123!",
    "email": "johndoe@example.com",
    "phoneNumber": "+2348012345678",
    "nationalIdentityNumber": "12345678901",
    "gender": "MALE",
    "role": "MANUFACTURING_EMPLOYEE",
    "dateOfBirth": "1990-01-01",
    "organisationId": "org-uuid-here",
    "organisationCode": "ORG123"
  }
  ```
- **Response (`201 Created`)**:
  ```json
  {
    "message": "User account created successfully",
    "data": {
      "id": "user-uuid-123",
      "username": "johndoe",
      "email": "johndoe@example.com",
      "role": "MANUFACTURING_EMPLOYEE"
    }
  }
  ```

#### `POST /api/v1/auth/signin`
Authenticates user credentials and issues an HMAC256-signed JWT token. Handled by `CustomAuthenticationFilter`.

- **Authorization**: Public (`Permit All`)
- **Request Body (`SignInRequest`)**:
  ```json
  {
    "username": "johndoe",
    "password": "Password123!"
  }
  ```
- **Response (`200 OK`)**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "authority": "MANUFACTURING_EMPLOYEE"
  }
  ```

---

### 2. Manufacturer Endpoints (`/api/v1/manufacturer`)

#### `POST /api/v1/manufacturer/drug`
Registers a new pharmaceutical product under the manufacturer's organization, including NAFDAC registration details and shelf-life expiration duration.

- **Authorization**: Required Role: `MANUFACTURING_EMPLOYEE`
- **Request Body (`CreateDrugRequest`)**:
  ```json
  {
    "brandName": "Paracetamol Extra",
    "genericName": "Paracetamol / Caffeine",
    "nafdacRegistrationNumber": "A4-1234",
    "description": "Fast-acting analgesic and antipyretic medication",
    "expirationDurationInDays": 730
  }
  ```
- **Response (`201 Created`)**: Returns `CreateDrugResponse` containing created drug details and system-generated drug code.

#### `POST /api/v1/manufacturer/batch`
Produces a new manufactured batch. Automatically generates unique cryptographic verification codes for the batch, all constituent packs, and all constituent sachets.

- **Authorization**: Required Role: `MANUFACTURING_EMPLOYEE`
- **Request Body (`CreateBatchRequest`)**:
  ```json
  {
    "drugId": "drug-uuid-123",
    "amountOfBatches": 1,
    "amountOfPacks": 100,
    "amountOfSachets": 10
  }
  ```
- **Response (`201 Created`)**: Returns `CreateBatchResponse` with generated batch ID, batch verification code, and counts.

#### `POST /api/v1/manufacturer/distribution/batch`
Transfers legal custody of a manufactured batch from the manufacturer to a licensed wholesaler or distributor organization.

- **Authorization**: Required Role: `MANUFACTURING_EMPLOYEE`
- **Request Body (`TransferBatchRequest`)**:
  ```json
  {
    "batchId": "batch-uuid-123",
    "receiverOrganisationId": "wholesaler-org-uuid"
  }
  ```
- **Response (`200 OK`)**: Returns `TransferBatchResponse` confirming batch transfer and logging the custody event in `BatchLogistics`.

---

### 3. Wholesaler Endpoints (`/api/v1/wholesaler`)

#### `POST /api/v1/wholesaler/distribution/batch`
Transfers an entire batch from one wholesaler to another wholesaler or licensed organization.

- **Authorization**: Required Role: `WHOLESALE_EMPLOYEE`
- **Request Body (`TransferBatchRequest`)**:
  ```json
  {
    "batchId": "batch-uuid-123",
    "receiverOrganisationId": "sub-distributor-org-uuid"
  }
  ```
- **Response (`200 OK`)**: Returns `TransferBatchResponse`.

#### `POST /api/v1/wholesaler/distribution/pack`
Transfers ownership of individual drug packs to downstream retailers (e.g., pharmacies, hospitals).

- **Authorization**: Required Role: `WHOLESALE_EMPLOYEE`
- **Request Body (`TransferPackRequest`)**:
  ```json
  {
    "packId": "pack-uuid-123",
    "receiverId": "retailer-org-or-user-uuid"
  }
  ```
- **Response (`200 OK`)**: Returns `TransferPackResponse` and records entry in `PackLogistics`.

---

### 4. Retailer Endpoints (`/api/v1/retailer`)

#### `POST /api/v1/retailer/distribution/pack`
Dispenses or transfers individual packs to consumers or fellow retail branches.

- **Authorization**: Required Role: `RETAIL_EMPLOYEE`
- **Request Body (`TransferPackRequest`)**:
  ```json
  {
    "packId": "pack-uuid-123",
    "receiverId": "consumer-user-uuid"
  }
  ```
- **Response (`200 OK`)**: Returns `TransferPackResponse`.

---

### 5. Consumer Endpoints (`/api/v1/consumer`)

#### `POST /api/v1/consumer/consumption/pack`
Marks a specific drug pack as consumed by the end-user, retiring the pack from further circulation.

- **Authorization**: Required Role: `CONSUMER`
- **Request Body (`TransferPackRequest`)**:
  ```json
  {
    "packId": "pack-uuid-123",
    "receiverId": "self"
  }
  ```
- **Response (`200 OK`)**: Returns `TransferPackResponse`.

---

### 6. Verification Endpoints (`/api/v1/verification`)

MedCheck returns structured domain models (`DrugDetail`, `PackDetail`, `SachetDetail`, and `LogisticsHistory`) to ensure full auditability.

#### `PUT /api/v1/verification/batch/{id}`
Verifies batch authenticity, manufacturing origin, and complete distribution custody trail using the batch verification code.

- **Authorization**: Required: `INVESTIGATOR`, `MANUFACTURING_EMPLOYEE`, `RETAIL_EMPLOYEE`, or `WHOLESALE_EMPLOYEE`
- **Path Parameter**: `id` (String) - Unique Batch Verification Code
- **Response (`200 OK`) (`VerifyBatchResponse`)**:
  ```json
  {
    "id": "batch-uuid-123",
    "verificationCode": "BTC-VERIFY-849204",
    "manufactureDate": "2026-01-15",
    "expiryDate": "2028-01-15",
    "noOfPacks": 100,
    "noOfSachetsPerPack": 10,
    "drugDetail": {
      "drugBrandName": "Paracetamol Extra",
      "genericName": "Paracetamol / Caffeine",
      "manufacturerName": "PharmaCorp Nigeria Ltd",
      "nafdacRegistrationNumber": "A4-1234",
      "description": "Fast-acting analgesic and antipyretic medication"
    },
    "logisticsHistory": [
      {
        "productType": "BATCH",
        "sender": "PharmaCorp Nigeria Ltd",
        "recipient": "Alpha Wholesalers Ltd",
        "timestamp": "2026-01-20T10:00:00Z"
      }
    ]
  }
  ```

#### `PUT /api/v1/verification/pack/{id}`
Verifies an individual pack unit code, increments the verification counter, and provides complete historical logistics tracking across both batch and pack movements.

- **Authorization**: Authenticated (Any valid JWT)
- **Path Parameter**: `id` (String) - Unique Pack Verification Code
- **Response (`200 OK`) (`VerifyPackResponse`)**:
  ```json
  {
    "id": "pack-uuid-456",
    "verificationCode": "PCK-VERIFY-928172",
    "verificationCount": 1,
    "manufactureDate": "2026-01-15",
    "expiryDate": "2028-01-15",
    "noOfSachets": 10,
    "drugDetail": {
      "drugBrandName": "Paracetamol Extra",
      "genericName": "Paracetamol / Caffeine",
      "manufacturerName": "PharmaCorp Nigeria Ltd",
      "nafdacRegistrationNumber": "A4-1234",
      "description": "Fast-acting analgesic and antipyretic medication"
    },
    "history": [
      {
        "productType": "BATCH",
        "sender": "PharmaCorp Nigeria Ltd",
        "recipient": "Alpha Wholesalers Ltd",
        "timestamp": "2026-01-20T10:00:00Z"
      },
      {
        "productType": "PACK",
        "sender": "Alpha Wholesalers Ltd",
        "recipient": "City Pharmacy Retail Ltd",
        "timestamp": "2026-01-25T14:30:00Z"
      }
    ]
  }
  ```

#### `PUT /api/v1/verification/sachet/{id}`
Verifies individual sachet unit codes entered or scanned by consumers or regulatory agents. Increments verification count and returns granular parent pack and drug details.

- **Authorization**: Authenticated (Any valid JWT)
- **Path Parameter**: `id` (String) - Unique Sachet Verification Code
- **Response (`200 OK`) (`VerifySachetResponse`)**:
  ```json
  {
    "verificationCode": "SCH-VERIFY-581920",
    "verificationCount": 1,
    "sachetdetail": {
      "sachetId": "sachet-uuid-789"
    },
    "packdetail": {
      "manufactureDate": "2026-01-15",
      "expiryDate": "2028-01-15"
    },
    "drugdetail": {
      "drugBrandName": "Paracetamol Extra",
      "genericName": "Paracetamol / Caffeine",
      "manufacturerName": "PharmaCorp Nigeria Ltd",
      "nafdacRegistrationNumber": "A4-1234",
      "description": "Fast-acting analgesic and antipyretic medication"
    },
    "logisticsHistory": [
      {
        "productType": "BATCH",
        "sender": "PharmaCorp Nigeria Ltd",
        "recipient": "Alpha Wholesalers Ltd",
        "timestamp": "2026-01-20T10:00:00Z"
      },
      {
        "productType": "PACK",
        "sender": "Alpha Wholesalers Ltd",
        "recipient": "City Pharmacy Retail Ltd",
        "timestamp": "2026-01-25T14:30:00Z"
      }
    ]
  }
  ```

---

### 7. Administrator Endpoints (`/api/v1/admin`)

- **Authorization**: Required Role: `ADMINISTRATOR`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/admin/organisation` | Creates and registers an organization (Manufacturer, Wholesaler, Retailer, Agency). |
| `GET` | `/api/v1/admin/organisation` | Lists all registered organizations. |
| `DELETE` | `/api/v1/admin/organisation` | Deactivates/removes an organization. |
| `GET` | `/api/v1/admin/user` | Lists all registered user accounts. |
| `DELETE` | `/api/v1/admin/user` | Removes a specific user account. |
| `PUT` | `/api/v1/admin/user/suspend` | Suspends a user account to block platform access immediately. |
| `PUT` | `/api/v1/admin/user/unsuspend` | Restores active status for a suspended user account. |

---

### 8. Investigator Endpoints (`/api/v1/investigator`)

- **Authorization**: Required Role: `INVESTIGATOR`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/investigator/organisation` | Audits organization details and records. |
| `GET` | `/api/v1/investigator/user` | Inspects user accounts across supply chain nodes to investigate suspicious transactions. |
| `PUT` | `/api/v1/investigator/user/suspend` | Flags and suspends accounts suspected of facilitating counterfeit distribution. |
| `PUT` | `/api/v1/investigator/user/unsuspend` | Restores account standing after investigative clearance. |

---

### 9. User Profile Endpoints (`/api/v1/profile`)

- **Authorization**: Authenticated (Any valid JWT)
- Base path for viewing user profile data, updating credentials, and uploading profile pictures via Cloudinary.

---

### 10. Health & Diagnostics (`/test`)

| Method | Endpoint | Authorization | Response |
|---|---|---|---|
| `GET` | `/test/live` | Public (`Permit All`) | Plain text: `"We are fucking live"` |
| `GET` | `/test/live/consumer` | Required Role: `CONSUMER` | Plain text: `"You are a consumer and you are active yami"` |
| `GET` | `/test/live/wholesaler` | Required Role: `WHOLESALE_EMPLOYEE` | Plain text: `"We are fucking live"` |
| `GET` | `/test/live/investigator` | Required Role: `INVESTIGATOR` | Plain text: `"We are fucking live"` |

---

## Error Handling

MedCheck implements centralized global exception handling via [`GlobalExceptionHandler`](src/main/java/com/yrsd/medcheck/utils/GlobalExceptionHandler.java) utilizing the RFC 7807 / RFC 9457 standard [`ProblemDetail`](https://datatracker.ietf.org/doc/html/rfc7807) response model:

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Email already exists",
  "instance": "/api/v1/auth/signup",
  "timestamp": "2026-10-27T08:15:30Z"
}
```

### Standard HTTP Status Codes

- `200 OK`: Request completed successfully.
- `201 Created`: Resource (Drug, Batch, User, Organisation) created successfully.
- `400 Bad Request`: Input validation error, invalid parameters, or malformed JSON payload.
- `401 Unauthorized`: Missing, expired, or invalid JWT authentication token.
- `403 Forbidden`: Authenticated user lacks the requisite role or account is suspended.
- `404 Not Found`: Entity not found (Batch, Pack, Sachet, Drug, User, or Organisation).
- `500 Internal Server Error`: Unhandled server exception.

---

## Project Directory Structure

```text
MedCheck-Server/
├── docker-compose.yml          # Container configuration (App, DB, Redis, RabbitMQ)
├── Dockerfile                  # Application multi-stage / container build definition
├── pom.xml                     # Maven project descriptor & dependencies
├── README.md                   # System documentation
├── .example.env                # Environment variable blueprint
├── src/
│   ├── main/
│   │   ├── java/com/yrsd/medcheck/
│   │   │   ├── config/         # Spring & 3rd-party configs (Brevo, Redis, RabbitMQ, Paystack)
│   │   │   ├── controllers/    # REST API Controllers (Auth, Manufacturer, Verification, etc.)
│   │   │   ├── data/
│   │   │   │   ├── models/     # JPA Entities (Drug, Batch, Pack, Sachet, UserAccount, etc.)
│   │   │   │   └── repositories/# Spring Data JPA repositories
│   │   │   ├── dtos/           # Request/response records & data transfer objects
│   │   │   ├── events/         # RabbitMQ publishers, consumers, & domain events
│   │   │   ├── exceptions/     # Custom domain exceptions
│   │   │   ├── interceptor/    # HTTP & logging interceptors (e.g. PaystackLoggingInterceptor)
│   │   │   ├── proxy/          # External client proxies (Cloudinary, Brevo, Paystack)
│   │   │   ├── security/       # Spring Security filters, managers, providers, configs
│   │   │   ├── services/       # Domain business logic & service implementations
│   │   │   └── utils/          # Code generation, global exception handlers, validators
│   │   └── resources/
│   │       ├── application.properties # Spring Boot property configurations
│   │       └── templates/      # HTML email templates
│   └── test/
│       └── java/com/yrsd/medcheck/ # Unit & integration test suites
```

---

## License

This project is licensed under the [MIT License](LICENSE).
