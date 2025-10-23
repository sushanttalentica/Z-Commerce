# Z-Commerce Platform

A comprehensive e-commerce platform built with Spring Boot, featuring multi-module architecture and microservices design.

## Architecture

This platform consists of multiple modules:

- **Core Module**: Core business logic and shared components
- **API Module**: REST API controllers and OpenAPI specifications
- **Application Module**: Main application configuration and startup
- **Payment Module**: Payment processing and gateway integration
- **Invoice Module**: Invoice generation and management
- **Inventory Module**: Stock management and inventory tracking
- **Notification Module**: Email and notification services

## Technology Stack

- Java 17
- Spring Boot 3.1.5
- Spring Security
- Spring Data JPA
- Hibernate
- Apache Kafka
- Redis
- PostgreSQL
- Docker
- Maven

## Getting Started

1. Clone the repository
2. Run `mvn clean install` to build all modules
3. Start the application with `mvn spring-boot:run` from the application module
4. Access the API documentation at `http://localhost:8080/swagger-ui.html`

## Key Features

- Multi-module Maven architecture
- RESTful API with OpenAPI documentation
- JWT-based authentication and authorization
- Event-driven architecture with Kafka
- Real-time notifications with WebSocket
- Payment processing integration
- Invoice generation with PDF support
- Inventory management
- Docker containerization

## Development

- Follow Java coding standards
- Use meaningful commit messages
- Write comprehensive tests
- Document API changes
- Follow the established module structure