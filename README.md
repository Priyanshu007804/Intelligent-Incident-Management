# Intelligent Incident Management System

A Spring Boot backend for managing IT incidents, user authentication, assignment workflows, and operational reporting. The application uses MongoDB for persistence, JWT for authentication, and Spring Security for authorization.

## Overview

This project is designed to support an incident management workflow where:

- users can register and log in
- incidents can be created, updated, assigned, and deleted
- admins and engineers can manage operational work
- dashboards summarize incident trends by category and priority
- the application exposes REST APIs and OpenAPI documentation

## Tech Stack

- Java 21
- Spring Boot 4.1.1
- Spring Web
- Spring Security
- Spring Data MongoDB
- JWT (jjwt)
- Validation
- Springdoc OpenAPI UI
- Maven

## Project Structure

```text
intelligent-incident-management/
├── src/
│   ├── main/
│   │   ├── java/com/priyanshu/iims/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   ├── service/
│   │   │   └── IntelligentIncidentManagementApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── Dockerfile
├── HELP.md
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## Features

### Authentication and authorization

- User registration
- User login
- JWT-based authentication
- Role-based access control for protected endpoints
- Security filter chain with stateless session management

### Incident management

- Create incident
- View all incidents with filtering
- Get incident by ID
- Update incident details
- Delete incident
- Assign incident to engineer

### Dashboard analytics

- Total incident summary
- Incidents by category
- Incidents by priority

### API documentation

- Swagger UI available through Springdoc OpenAPI

## Prerequisites

Before running the project, ensure you have:

- Java 21 or newer installed
- Maven installed, or use the included Maven wrapper (`mvnw`)
- MongoDB running locally or a reachable MongoDB connection string

## Configuration

The application reads configuration from `src/main/resources/application.properties`.

```properties
spring.application.name=intelligent-incident-management

spring.mongodb.uri=${MONGODB_URI}
spring.mongodb.database=iims_db

jwt.secret=${JWT_SECRET}
server.port=${PORT:8080}
```

### Required environment variables

```bash
MONGODB_URI=mongodb://localhost:27017/iims_db
JWT_SECRET=your-very-secret-key
PORT=8080
```

If you do not configure `MONGODB_URI` and `JWT_SECRET`, the app will not have a valid database or signing key in a real environment.

## Running the Application

### Using Maven wrapper

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

### Or build and run the JAR

```bash
./mvnw clean package
java -jar target/intelligent-incident-management-0.0.1-SNAPSHOT.jar
```

The server will run on:

```text
http://localhost:8080
```

## Health Check

```http
GET /health
```

Example response:

```text
IIMS Backend is healthy
```

## API Endpoints

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
```

### Incidents

```http
POST /api/incidents
GET /api/incidents
GET /api/incidents/{id}
PUT /api/incidents/update/{id}
DELETE /api/incidents/delete/{id}
PUT /api/incidents/assign/{id}
```

### Dashboard

```http
GET /api/dashboard/summary
GET /api/dashboard/by-category
GET /api/dashboard/by-priority
```

### OpenAPI / Swagger

```http
GET /swagger-ui/index.html
GET /v3/api-docs
```

## Security Notes

- The app uses stateless JWT-based security.
- Public endpoints include health check, auth endpoints, and Swagger documentation.
- Most incident and dashboard endpoints require authentication.
- Some write operations are restricted by role checks such as `ADMIN` and `ENGINEER`.

## Typical Development Workflow

1. Start MongoDB.
2. Set `MONGODB_URI` and `JWT_SECRET` in your environment.
3. Run the Spring Boot application.
4. Test the endpoints with Postman, curl, or Swagger UI.
5. Monitor logs and actuator endpoints for application health.

## Docker

A `Dockerfile` is included in the project root. You can use it to containerize the application if needed.

## License

This project does not include a custom license file in the repository, so default project usage should follow your local organization or project policy unless otherwise specified.

## Notes for a First-Time Spring Boot Project

This project is a solid beginner-to-intermediate Spring Boot backend example because it demonstrates:

- dependency injection
- MVC controllers
- repository layer with MongoDB
- DTOs and validation
- JWT authentication
- Spring Security configuration
- REST API design
- dashboard aggregation logic

If you are learning Spring Boot, this project is a good reference for building a real-world backend service.
