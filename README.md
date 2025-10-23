# Z-Commerce Platform

A comprehensive e-commerce platform built with Spring Boot, Java 21, and modern microservices architecture.

## Features

- **User Management**: Customer registration, authentication, and authorization
- **Product Management**: CRUD operations for products and categories
- **Order Management**: Complete order lifecycle management
- **Payment Processing**: Secure payment gateway integration
- **Invoice Generation**: Automated invoice creation and management
- **Real-time Notifications**: WebSocket and Kafka-based notifications
- **Inventory Management**: Stock tracking and updates
- **Security**: JWT-based authentication with role-based access control

## Architecture

- **Microservices**: Modular architecture with separate concerns
- **Event-Driven**: Kafka-based event streaming
- **Database**: H2 in-memory database for development
- **Caching**: Redis for performance optimization
- **Real-time**: WebSocket for live updates

## Technology Stack

- Java 21
- Spring Boot 3.1.5
- Spring Security
- Spring Data JPA
- Kafka
- Redis
- H2 Database
- Maven
- Docker

## Getting Started

1. Clone the repository
2. Ensure Java 21 is installed
3. Run `mvn clean install`
4. Start the application with `mvn spring-boot:run`

## API Documentation

Swagger UI is available at: `http://localhost:8080/swagger-ui/index.html`

## License

This project is licensed under the MIT License.