# Xopix E-commerce: User Service

The **Xopix User Service** is a core microservice responsible for managing user accounts and profiles within the Xopix E-commerce platform. It handles user registration, profile updates, and provides user data for other services. Authentication and authorization are primarily managed externally by Auth0 and the Kong API Gateway.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [API Endpoints](#api-endpoints)
- [Local Development Setup](#local-development-setup)
- [Database Migrations (Flyway)](#database-migrations-flyway)
- [Contributing](#contributing)
- [License](#license)

## Features

- User registration (email and password)
- User profile retrieval by internal ID
- User profile updates (first name, last name, phone number)
- Secure password hashing using BCrypt
- Integration point for Auth0 user IDs
- Robust error handling for common scenarios (e.g., user not found, email already exists)

## Architecture

This service is part of the larger Xopix E-commerce microservices architecture.

- **Data Ownership:** Owns its data in a dedicated MySQL database.
- **API Gateway:** Exposed via the Kong API Gateway.
- **Authentication:** Relies on Auth0 (via Kong API Gateway) for user authentication; the service expects user context (e.g., user ID) to be passed securely from the gateway.
- **Service Mesh:** Operates within the Service Mesh (Envoy), benefiting from mTLS, advanced traffic management, and enhanced observability for inter-service communication.

For a detailed overview of the entire Xopix architecture, please refer to the main [Xopix E-commerce: Architectural Goals & Implementation Plan](../docs/ARCHITECTURE_PLAN.md) (adjust path if your docs are in a different repo/folder).

## Technologies

- **Language:** Java 17+
- **Framework:** Spring Boot 3.2.x
- **Database:** MySQL
- **ORM:** Spring Data JPA / Hibernate
- **Database Migrations:** Flyway
- **Build Tool:** Maven
- **Dependency Management:** Lombok
- **Security:** Spring Security (for password encoding, basic endpoint security)

## API Endpoints

The User Service exposes RESTful APIs. For the full API specification, refer to `openapi.yaml` in the `docs/api` folder of this repository.

| Method | Endpoint                    | Description                                  | Authentication |
| ------ | --------------------------- | -------------------------------------------- | -------------- |
| `POST` | `/api/users`                | Register a new user account.                 | None           |
| `GET`  | `/api/users/{userId}`       | Retrieve a user's profile by their internal ID. | JWT (Auth0)    |
| `PUT`  | `/api/users/{userId}`       | Update an existing user's profile.           | JWT (Auth0)    |
| `POST` | `/api/users/_internal/associate-auth0` | Internal endpoint to link Auth0 user ID to existing user (Highly Secured) | Internal Only |

### Authentication Note:
While Spring Security is included for password encoding and basic endpoint protection, the primary JWT validation and authorization enforcement for Xopix's public-facing APIs occur at the **Kong API Gateway** (integrated with Auth0). The User Service trusts the user context passed to it by the API Gateway after successful authentication.

## Local Development Setup

To run the User Service locally:

1.  **Prerequisites:**
    * Java 17+ SDK installed.
    * Maven 3.x installed.
    * MySQL Server (8.x recommended) running locally.
    * Docker and Docker Compose for easy MySQL setup.

2.  **Database Setup (using Docker Compose for MySQL):**
    Create a `docker-compose.yml` file in your project root (or a shared `devops` folder):
    ```yaml
    version: '3.8'
    services:
      mysql_user_db:
        image: mysql:8.0
        environment:
          MYSQL_ROOT_PASSWORD: password
          MYSQL_DATABASE: xopix_user_db
        ports:
          - "3306:3306"
        volumes:
          - mysql_user_data:/var/lib/mysql
        healthcheck:
          test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
          timeout: 20s
          retries: 10
    volumes:
      mysql_user_data:
    ```
    Start the MySQL container:
    ```bash
    docker-compose up -d mysql_user_db
    ```

3.  **Configure `application.properties`:**
    Ensure your `src/main/resources/application.properties` matches your local MySQL setup (default provided in code is for the above Docker Compose).

4.  **Run Flyway Migrations:**
    Flyway migrations will run automatically when Spring Boot starts if `spring.flyway.enabled=true`. Ensure your `db/migration` folder contains the `V1__Create_users_table.sql` script.

5.  **Build and Run the Application:**
    Navigate to the `user-service` project root in your terminal and run:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    The service should start on `http://localhost:8080`.

6.  **Test Endpoints (e.g., using curl or Postman):**

    * **Register User:**
        ```bash
        curl -X POST http://localhost:8080/api/users \
        -H "Content-Type: application/json" \
        -d '{
            "email": "testuser@xopix.com",
            "password": "strongPassword123",
            "firstName": "Test",
            "lastName": "User",
            "phoneNumber": "123-456-7890"
        }'
        ```
    * **Get User (replace `{userId}` with actual ID from registration response):**
        ```bash
        curl -X GET http://localhost:8080/api/users/{userId}
        # In a real scenario, this would be authenticated via JWT
        ```