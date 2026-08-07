# MedCheck-Server

## Overview
MedCheck is an enterprise-grade pharmaceutical anti-counterfeiting backend system designed for the Nigerian supply chain ecosystem. It provides secure, transparent, and immutable tracking of pharmaceutical products across all tiers of distribution—from Manufacturers and Wholesalers to Retailers, Regulatory Investigators, and end Consumers.

The system utilizes cryptographic verification codes on Batches, Packs, and Sachets to prevent counterfeit drugs from entering circulation, ensuring authentic medication reaches patients.

---

## Core Features
- **End-to-End Supply Chain Traceability**: Complete tracking of pharmaceutical hierarchy (Batch to Pack to Sachet) from factory floor to consumer consumption.
- **Role-Based Access Control (RBAC)**: Fine-grained access control enforcing distinct permissions for Administrators, Manufacturers, Wholesalers, Retailers, Investigators, and Consumers.
- **Stateless JWT Authentication**: Custom Spring Security filter pipeline handling secure JSON Web Tokens with secret signing and configurable time-to-live.
- **Hierarchical Batch & Code Management**: Automatic generation and verification of unique codes for batches, individual packs, and sachets.
- **Asynchronous Event Processing**: Integration with RabbitMQ for event-driven workflows such as registration notifications and audit logging.
- **Distributed Caching**: Redis integration for fast verification code lookups and performance optimization.
- **Email Notifications**: Integration with Brevo API for system notifications and user verification alerts.

---

## Technology Stack
- **Core Platform**: Java 21, Spring Boot 3.x
- **Security & Auth**: Spring Security 6, Auth0 JWT (HMAC256)
- **Database**: PostgreSQL 16 (JPA / Hibernate ORM)
- **Caching**: Redis 8.0
- **Message Broker**: RabbitMQ 4 (AMQP Protocol)
- **Email Provider**: Brevo (Sendinblue) API
- **Containerization & Orchestration**: Docker, Docker Compose
- **Build System**: Maven 3.9

---

## System Architecture

MedCheck follows Domain-Driven Design (DDD) principles with a clean layered architecture:
- **Presentation Layer (Controllers)**: REST APIs exposing endpoints for supply chain operations.
- **Security Layer (Filters & Handlers)**: Custom JWT Authentication and Authorization filters parsing headers and enforcing RBAC.
- **Service Layer (Business Logic)**: Domain services handling batch creation, code generation, ownership transfers, verification logic, and external service communication.
- **Repository Layer (Data Access)**: Spring Data JPA repositories interfacing with PostgreSQL.
- **Infrastructure Layer**: Redis for caching, RabbitMQ for messaging queues, Cloudinary for asset storage, and Brevo for email delivery.

---

## Environment Variables Configuration

Copy `.example.env` to `.env` and fill in the required parameters before running the application:

```env
# Database Configuration
DATABASE_URL=jdbc:postgresql://db:5432/medcheck_db
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_secure_password
DATABASE_PORT=5432

# Application Server Port
SERVER_PORT=8080

# Security & JWT Settings
JWT_SIGNING_KEY=your_jwt_secret_key
JWT_DURATION_IN_SECONDS=86400
VERIFICATION_CODE_LENGTH=12

# Redis Cache Settings
REDIS_HOST=redis
REDIS_PORT=6379
TIME_TO_LIVE_IN_HOURS=24

# RabbitMQ Settings
RABBITMQ_HOST=rabbitmq
RABBITMQ_USER=guest
RABBITMQ_PASSWORD=guest
RABBITMQ_PORT=5672
RABBITMQ_UI_PORT=15672
RABBITMQ_EXCHANGE_NAME=medcheck_exchange
RABBITMQ_USER_REGISTERED_ROUTING_KEY=user.registered
RABBITMQ_QUEUE_NAME=medcheck_queue

# Third-Party Integrations
CLOUDINARY_URL=cloudinary://api_key:api_secret@cloud_name
BREVO_API_KEY=your_brevo_api_key
BREVO_EMAIL_SENDER=no-reply@medcheck.ng
```

---

## Getting Started & Installation

### Option 1: Running with Docker Compose (Recommended)
1. Clone the repository:
   ```bash
   git clone https://github.com/TheDurodola/MedCheck-Server.git
   cd MedCheck-Server
   ```
2. Create and configure your environment file:
   ```bash
   cp .example.env .env
   ```
3. Build and launch all services (MedCheck Server, PostgreSQL, Redis, RabbitMQ):
   ```bash
   docker-compose up --build -d
   ```
4. Access the server at `http://localhost:8081` (or your configured `SERVER_PORT`).

### Option 2: Local Development Setup
1. Ensure Java 21 JDK, Maven, PostgreSQL, Redis, and RabbitMQ are installed and running.
2. Update `.env` or application properties with your local database and service credentials.
3. Build the project:
   ```bash
   ./mvnw clean package -DskipTests
   ```
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## Security & Role-Based Access Control (RBAC)

The application enforces strict authorization based on user roles assigned during registration:
- `ADMINISTRATOR`: System administration, organization onboarding, user account management (suspension/unsuspension).
- `MANUFACTURING_EMPLOYEE`: Drug registration, batch creation, batch distribution to wholesalers.
- `WHOLESALE_EMPLOYEE`: Bulk batch distribution, pack distribution to retail outlets.
- `RETAIL_EMPLOYEE`: Pack distribution, retail sales, pack verification.
- `INVESTIGATOR`: Regulatory audits, user suspension, global verification of batches, packs, and sachets.
- `CONSUMER`: Pack verification, sachet verification, marking pack consumption.

Authentication Header Format:
```http
Authorization: Bearer <your_jwt_token>
```

---

## Comprehensive API Reference

Below is the complete description of every REST endpoint available in MedCheck Server.

### 1. Authentication Endpoints (`/api/v1/auth`)

#### POST `/api/v1/auth/signup`
- **Description**: Registers a new user on the MedCheck platform. Assigns user roles (e.g., CONSUMER, MANUFACTURING_EMPLOYEE, WHOLESALE_EMPLOYEE, RETAIL_EMPLOYEE, INVESTIGATOR, ADMINISTRATOR) and links the user to an organization if applicable.
- **Authorization**: Public (Permit All)
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
    "data": { ... },
    "message": "User account created successfully"
  }
  ```

#### POST `/api/v1/auth/signin`
- **Description**: Authenticates user credentials and issues a signed JWT token upon successful authentication. Intercepted by `CustomAuthenticationFilter`.
- **Authorization**: Public (Permit All)
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

#### POST `/api/v1/manufacturer/drug`
- **Description**: Registers a new drug product under the manufacturer's portfolio, including NAFDAC registration details and shelf-life expiration duration.
- **Authorization**: Required Role: `MANUFACTURING_EMPLOYEE`
- **Request Body (`CreateDrugRequest`)**:
  ```json
  {
    "brandName": "Paracetamol Extra",
    "genericName": "Paracetamol / Caffeine",
    "nafdacRegistrationNumber": "A4-1234",
    "description": "Pain relief medication",
    "expirationDurationInDays": 730
  }
  ```
- **Response (`201 Created`)**: Returns `CreateDrugResponse` with created drug metadata.

#### POST `/api/v1/manufacturer/batch`
- **Description**: Generates a new manufactured drug batch. Automatically generates hierarchical verification unit codes for batches, packs, and sachets.
- **Authorization**: Required Role: `MANUFACTURING_EMPLOYEE`
- **Request Body (`CreateBatchRequest`)**:
  ```json
  {
    "drugId": "drug-uuid-123",
    "amountOfBatches": 1,
    "amountOfPacks": 100,
    "amountOfSachets": 1000
  }
  ```
- **Response (`201 Created`)**: Returns `CreateBatchResponse` with generated batch ID and verification codes.

#### POST `/api/v1/manufacturer/distribution/batch`
- **Description**: Transfers ownership of a manufactured drug batch to a registered distributor or wholesaler.
- **Authorization**: Required Role: `MANUFACTURING_EMPLOYEE`
- **Request Body (`TransferBatchRequest`)**:
  ```json
  {
    "batchId": "batch-uuid-123",
    "receiverOrganisationId": "wholesaler-org-uuid"
  }
  ```
- **Response (`200 OK`)**: Returns `TransferBatchResponse` confirming batch transfer.

---

### 3. Wholesaler Endpoints (`/api/v1/wholesaler`)

#### POST `/api/v1/wholesaler/distribution/batch`
- **Description**: Allows a wholesaler to transfer an entire drug batch to another licensed wholesaler or retailer organization.
- **Authorization**: Required Role: `WHOLESALE_EMPLOYEE`
- **Request Body (`TransferBatchRequest`)**:
  ```json
  {
    "batchId": "batch-uuid-123",
    "receiverOrganisationId": "retailer-org-uuid"
  }
  ```
- **Response (`200 OK`)**: Returns `TransferBatchResponse` confirming batch distribution.

#### POST `/api/v1/wholesaler/distribution/pack`
- **Description**: Transfers ownership of individual drug packs to downstream supply chain entities such as retail pharmacies.
- **Authorization**: Required Role: `WHOLESALE_EMPLOYEE`
- **Request Body (`TransferPackRequest`)**:
  ```json
  {
    "packId": "pack-uuid-123",
    "receiverId": "retailer-user-or-org-uuid"
  }
  ```
- **Response (`200 OK`)**: Returns `TransferPackResponse` containing pack transfer details.

---

### 4. Retailer Endpoints (`/api/v1/retailer`)

#### POST `/api/v1/retailer/distribution/pack`
- **Description**: Allows retailers to transfer or dispense individual drug packs to other retail branches or consumers.
- **Authorization**: Required Role: `RETAIL_EMPLOYEE`
- **Request Body (`TransferPackRequest`)**:
  ```json
  {
    "packId": "pack-uuid-123",
    "receiverId": "consumer-user-uuid"
  }
  ```
- **Response (`200 OK`)**: Returns `TransferPackResponse` confirming retail transfer.

---

### 5. Consumer Endpoints (`/api/v1/consumer`)

#### POST `/api/v1/consumer/consumption/pack`
- **Description**: Marks a specific drug pack as consumed by the end-user, updating its status in the supply chain lifecycle to prevent re-use.
- **Authorization**: Required Role: `CONSUMER`
- **Request Body (`TransferPackRequest`)**:
  ```json
  {
    "packId": "pack-uuid-123",
    "receiverId": "self"
  }
  ```
- **Response (`200 OK`)**: Returns `TransferPackResponse` with updated pack consumption state.

---

### 6. Verification Endpoints (`/api/v1/verification`)

#### PUT `/api/v1/verification/batch/{id}`
- **Description**: Verifies the authenticity, manufacturing origin, and current status of a pharmaceutical batch using its verification code.
- **Authorization**: Required Roles: `INVESTIGATOR`, `MANUFACTURER_EMPLOYEE`, `RETAIL_EMPLOYEE`, `WHOLESALE_EMPLOYEE`
- **Path Parameter**: `id` (String) - Unique Batch Verification Code
- **Response (`200 OK`)**: Returns `VerifyBatchResponse` detailing batch legitimacy, manufacturer name, NAFDAC number, and expiration.

#### PUT `/api/v1/verification/pack`
- **Description**: Verifies an individual drug pack using its verification code to check for counterfeiting or illicit supply chain insertion.
- **Authorization**: Required: Any Authenticated User
- **Request Body (`VerifyPackRequest`)**:
  ```json
  {
    "packVerificationCode": "PACK-VERIFY-12345"
  }
  ```
- **Response (`200 OK`)**: Returns `VerifyPackResponse` confirming pack authenticity and parent batch information.

#### PUT `/api/v1/verification/sachet/{id}`
- **Description**: Verifies an individual sachet unit code scanned or entered by an investigator, healthcare worker, or consumer.
- **Authorization**: Required: Any Authenticated User
- **Path Parameter**: `id` (String) - Unique Sachet Verification Code
- **Response (`200 OK`)**: Returns `VerifySachetResponse` confirming sachet authenticity, expiry, and drug details.

---

### 7. Administrator Endpoints (`/api/v1/admin`)

#### POST `/api/v1/admin/organisation`
- **Description**: Creates and registers a new organization profile (Manufacturer, Wholesaler, Retailer, Regulatory Agency) in the MedCheck network.
- **Authorization**: Required Role: `ADMINISTRATOR`
- **Request Body (`CreateOrganisationRequest`)**:
  ```json
  {
    "name": "PharmaCorp Nigeria Ltd",
    "organizationType": "MANUFACTURER"
  }
  ```
- **Response (`201 Created`)**: Returns `CreateOrganisationResponse` with organisation ID and generated organisation code.

#### GET `/api/v1/admin/organisation`
- **Description**: Fetches all registered organizations within the platform for system oversight.
- **Authorization**: Required Role: `ADMINISTRATOR`
- **Response (`200 OK`)**: Returns a list of organizations.

#### DELETE `/api/v1/admin/organisation`
- **Description**: Deactivates or removes an organization from the MedCheck ecosystem.
- **Authorization**: Required Role: `ADMINISTRATOR`
- **Response (`200 OK`)**: Confirmation of organisation removal.

#### GET `/api/v1/admin/user`
- **Description**: Retrieves all user accounts across all roles and organizations for system management.
- **Authorization**: Required Role: `ADMINISTRATOR`
- **Response (`200 OK`)**: List of registered user accounts.

#### DELETE `/api/v1/admin/user`
- **Description**: Removes a specific user account from the system.
- **Authorization**: Required Role: `ADMINISTRATOR`
- **Response (`200 OK`)**: Confirmation of user deletion.

#### PUT `/api/v1/admin/user/suspend`
- **Description**: Suspends a user account to block access in response to security or compliance violations.
- **Authorization**: Required Role: `ADMINISTRATOR`
- **Response (`200 OK`)**: Updated user suspension status.

#### PUT `/api/v1/admin/user/unsuspend`
- **Description**: Restores active access for a previously suspended user account.
- **Authorization**: Required Role: `ADMINISTRATOR`
- **Response (`200 OK`)**: Updated user active status.

---

### 8. Investigator Endpoints (`/api/v1/investigator`)

#### GET `/api/v1/investigator/organisation`
- **Description**: Allows regulatory investigators (e.g., NAFDAC, law enforcement) to retrieve organization records for auditing.
- **Authorization**: Required Role: `INVESTIGATOR`
- **Response (`200 OK`)**: Detailed organisation profiles.

#### GET `/api/v1/investigator/user`
- **Description**: Fetches user profiles across supply chain nodes to investigate suspicious transactions or drug counterfeiting flags.
- **Authorization**: Required Role: `INVESTIGATOR`
- **Response (`200 OK`)**: List of supply chain user accounts.

#### PUT `/api/v1/investigator/user/suspend`
- **Description**: Allows an investigator to flag and suspend a user account suspected of facilitating counterfeit drug distribution.
- **Authorization**: Required Role: `INVESTIGATOR`
- **Response (`200 OK`)**: Suspension status update.

#### PUT `/api/v1/investigator/user/unsuspend`
- **Description**: Reinstates a user account after investigation clearance.
- **Authorization**: Required Role: `INVESTIGATOR`
- **Response (`200 OK`)**: Account reinstatement status.

---

### 9. User Profile Endpoints (`/api/v1/profile`)

#### Base Route `/api/v1/profile`
- **Description**: Base endpoint path for managing authenticated user profile details, credentials, and profile images.
- **Authorization**: Required: Any Authenticated User
- **Response (`200 OK`)**: User profile summary and attributes.

---

### 10. Health & Test Endpoints (`/test`)

#### GET `/test/live`
- **Description**: Public health check endpoint to confirm application server live state.
- **Authorization**: Public (Permit All)
- **Response (`200 OK`)**: Plain text `"We are fucking live"`

#### GET `/test/live/consumer`
- **Description**: Health check endpoint verifying authorization rules for the Consumer role.
- **Authorization**: Required Role: `CONSUMER`
- **Response (`200 OK`)**: Plain text `"You are a consumer and you are active yami"`

#### GET `/test/live/wholesaler`
- **Description**: Health check endpoint verifying authorization rules for Wholesalers.
- **Authorization**: Required Role: `WHOLESALE_EMPLOYEE`
- **Response (`200 OK`)**: Plain text `"We are fucking live"`

#### GET `/test/live/investigator`
- **Description**: Health check endpoint verifying authorization rules for Investigators.
- **Authorization**: Required Role: `INVESTIGATOR`
- **Response (`200 OK`)**: Plain text `"We are fucking live"`

---

## Error Handling

MedCheck implements a centralized exception handling mechanism (`GlobalExceptionHandler`) returning standardized JSON error responses:

```json
{
  "timestamp": "2026-08-07T11:31:11",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for field: brandName",
  "path": "/api/v1/manufacturer/drug"
}
```

Common HTTP status codes returned by the API:
- `200 OK`: Request processed successfully.
- `201 Created`: Resource created successfully.
- `400 Bad Request`: Validation failure or invalid input data.
- `401 Unauthorized`: Missing or invalid JWT authentication token.
- `403 Forbidden`: Authenticated user lacks required role/permissions.
- `404 Not Found`: Requested resource (Drug, Batch, Pack, Sachet, User, Organisation) not found.
- `500 Internal Server Error`: Unhandled server exception.

---

## License

This project is licensed under the MIT License - see the LICENSE file for details.
