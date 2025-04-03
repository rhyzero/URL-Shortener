# URL Shortener Application

A simple URL shortener application built with Spring Boot.

## Features

- Shorten long URLs into easy-to-share short links
- Track the number of clicks on each shortened URL
- User authentication with role-based access control
- Admin dashboard for URL management

## Technologies Used

- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL Database
- Thymeleaf Templates
- Docker

## Running with Docker

### Prerequisites

- Docker
- Docker Compose

### Building and Running

1. Build the application using Maven:

   ```
   ./mvnw clean package
   ```

2. Start the application using Docker Compose:

   ```
   docker-compose up -d
   ```

3. The application will be available at http://localhost:8080

### Docker Commands

- Build the Docker image:

  ```
  docker build -t url-shortener .
  ```

- Run the container:
  ```
  docker run -p 8080:8080 url-shortener
  ```

## Running Locally (without Docker)

1. Make sure you have MySQL running locally with the following configuration:

   - Database: url_shortener
   - Username: root
   - Password: root (or update application.properties with your credentials)

2. Run the application with Maven:
   ```
   ./mvnw spring-boot:run
   ```

## Default Users

- Admin: username `admin`, password `admin`
- Regular user: username `user`, password `user`
