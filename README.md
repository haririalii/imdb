# IMDB Project

## Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Tech Stack](#tech-stack)
4. [Architecture](#architecture)
5. [Installation](#installation)
6. [Usage](#usage)
7. [Configuration](#configuration)
8. [Endpoints](#endpoints)

## Overview
The IMDB project is a comprehensive Java-based application designed for managing and viewing movie-related data. It offers a wide range of functionalities, from data import/export to real-time metrics monitoring. Built with a modern stack, it provides a robust and user-friendly platform for movie enthusiasts and database managers alike.

## Features
- **Data Import/Export**: Easily import and export movie data in various formats.
- **Search and Filter**: Powerful search and filtering capabilities for movies.
- **Metrics Monitoring**: Real-time monitoring of user metrics and application performance.
- **API Request Counting**: Track and analyze API interactions.
- **User Management**: Secure user authentication and authorization.

## Tech Stack
The project leverages the following technologies and frameworks:
- **Java 21**: The core programming language.
- **Jakarta EE**: Enterprise features and APIs.
- **Spring Boot**: Application framework for managing dependencies and configurations.
  - **Spring Data JPA**: Simplified database interactions.
  - **Spring MVC**: Framework for building RESTful web services.
- **Lombok**: Reduces boilerplate code for model classes.
- **Maven**: Dependency management and build automation.
- **H2 Database**: In-memory database for development and testing.
- **Actuator**: For monitoring and managing the application in production.

## Architecture
The application follows a layered architecture:
- **Controller Layer**: Handles incoming HTTP requests and returns responses.
- **Service Layer**: Contains business logic and service implementations.
- **Repository Layer**: Handles data access and interacts with the database.
- **Model Layer**: Represents the data structures used in the application.

## Installation
### Prerequisites
- Java 21 installed and `JAVA_HOME` configured.
- Maven installed.

### Steps
1. Clone the repository:
    ```sh
    git clone <repository-url>
    cd imdb
    ```
2. Install dependencies using Maven:
    ```sh
    mvn clean install
    ```

## Usage
To run the application locally:
```sh
mvn spring-boot:run
```
The application will start on `http://localhost:8080`.

## Configuration
Configuration for the application can be customized in the `application.yaml` file located in `src/main/resources/`. Key configurations include:

- **Database Settings**:
  ```yaml
  spring:
    datasource:
      url: jdbc:h2:mem:testdb
      driverClassName: org.h2.Driver
      username: sa
      password: password
  ```

- **Server Settings**:
  ```yaml
  server:
    port: 8080
  ```

## Endpoints
### Import and Data Management
- **POST /api/import/title_ratings**: Imports the dataset into the application.
  
  Example:
  ```sh
  curl -X POST http://localhost:8080/api/import/title_ratings
  ```

### Movie Queries
- **GET /api/titles?same_director_writer=true&alive=true**: Returns all titles in which both director and writer are the same person and he/she is still alive.
  
  Example:
  ```sh
  curl -X GET "http://localhost:8080/api/titles?same_director_writer=true&alive=true"
  ```

- **GET /api/titles?actor=nm0000190&actor=nm0004266**: Gets two actors and returns all titles in which both of them played.
  
  Example:
  ```sh
  curl -X GET "http://localhost:8080/api/titles?actor=nm0000190&actor=nm0004266"
  ```

- **GET /api/titles/genre/{genre}**: Gets a genre from the user and returns the best titles of each year for that genre based on the number of votes and rating.
  
  Example:
  ```sh
  curl -X GET "http://localhost:8080/api/titles/genre/Drama"
  ```

### Metrics
- **GET /api/metrics/http_requests**: Counts how many HTTP requests were received by the application since the last startup.
  
  Example:
  ```sh
  curl -X GET http://localhost:8080/api/metrics/http_requests
  ```