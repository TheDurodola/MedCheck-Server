# MedCheck-Server
- MedCheck is a robust backend system designed to combat drug counterfeiting in Nigeria. It provides a secure, transparent supply chain tracking system connecting Manufacturers, Wholesalers, Retailers, and Consumers.

# Key Features
Supply Chain Integrity: End-to-end tracking of pharmaceutical batches from factory to pharmacy.

Role-Based Access Control (RBAC): Secure authorization for different entities (Manufacturer, Wholesaler, Retailer, Admin).

Secure Authentication: JWT-based stateless authentication with custom security filters.

Batch Management: efficient logistics handling for PackLogistics and batch transfers.


# Tech Stack
Language: Java 21

Framework: Spring Boot 3 (Web, Data JPA, Security, Validation)

Database: PostgreSQL (Production)

Containerization: Docker 

Testing: JUnit 5, Mockito

Build Tool: Maven


# Architecture
The project follows a Domain-Driven Design (DDD) approach with a clean layered architecture:

Controllers: REST API endpoints.

Services: Business logic and transaction management.

Repositories: Data access layer (Hibernate/JPA).

Security: Custom JWT filters and Authentication Providers.
