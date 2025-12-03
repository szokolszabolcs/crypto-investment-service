# Crypto Investment Service

A Spring Boot application for analyzing and exposing cryptocurrency statistics from CSV data files. The app provides
REST endpoints for normalized range calculations, crypto statistics, and more, with rate limiting and robust error
handling.

---

## Features

- **CSV Data Ingestion:** Reads crypto price data from CSV files on startup and loads it into an H2 database.
- **REST API:** Exposes endpoints for:
    - Listing cryptos sorted by normalized range
    - Getting stats (oldest, newest, min, max) for a specific crypto
    - Finding the crypto with the highest normalized range for a given day
- **Rate Limiting:** Limits requests per IP using Bucket4j.
- **Swagger/OpenAPI Documentation:** Interactive API docs available.

---

## How to Run

### 1. Prerequisites

- Java 25 or higher
- Maven 3.8+ (for building)
- Docker (optional, for containerized runs)

### 2. Build the Application

```bash
mvn clean package
```

### 3. Run the Application

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--crypto.prices.dir=C:\prices"
```

**Note:**
Replace `C:\prices` with the path to the directory containing your CSV files with crypto price data.

Each CSV file should have the following format:

```
timestamp,symbol,price
1640995200000,BTC,45678.91
```

The application startup will fail if the directory is not provided.

### 4. Run with Docker

Build the Docker image:

```bash
docker build -t crypto-investment-service .
```

Run the container:

```bash
docker run -p 8080:8080 -v /my_path/prices:/data/prices crypto-investment-service --crypto.prices.dir=/data/prices
```

**Note:**
Please replace the mounted `/my_path/prices` path with the one which contains the CSV files as described in the **Run
the Application** section.

### 5. Configuration

- **CSV Data Directory:**  
  The folder containing the crypto prices. Should be provided as the `crypto.prices.dir` program argument (e.g.
  `--crypto.prices.dir=C:\prices`), otherwise the program start will fail.
- **Database:**  
  Uses in-memory H2 by default. See improvement points for production DB options.

## API Documentation

- **Swagger UI:**  
  Once the app is running, access the interactive API docs at:  
  [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
  or  
  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Code Structure

- **controller/** – REST controllers (API endpoints)
- **exception/** – Custom exceptions and global exception handler
- **filter/** – Servlet filters (e.g., IP rate limiting)
- **model/**
    - **domain/** – Domain objects (business logic)
    - **dto/** – Data Transfer Objects (API layer, CSV, etc.)
    - **entity/** – JPA entities
    - **mapper/** – Mapper classes for DTO <-> Entity <-> Domain
- **repository/** – Spring Data JPA repositories
- **service/** – Business logic and data loading services

## Possible Improvement Points

- **Add Code Coverage Checking:**  
  Integrate tools like JaCoCo or Cobertura to monitor and improve test coverage.
- **Add missing tests:**  
  Currently there is only an integration test to check the basic functionality of the endpoints and there are test for
  the services, but other unit tests are missing.
- **More JavaDoc:**  
  JavaDoc is currently added only to the services, but it could be beneficial to documents other classes as well.
- **Auto Linting Tool:**  
  Use Checkstyle, Spotless, or similar tools to enforce code style and quality automatically.
- **Customize Logging:**  
  Enhance logback configuration for better log management, structured logging, and external log aggregation.
- **Introduce Monitoring:**  
  Add application monitoring with tools like Spring Boot Actuator, Prometheus, and Grafana.
- **Track Database Changes:**  
  Use Liquibase or Flyway to manage and version database schema changes.
- **Switch to a Production-Grade Database:**  
  Replace H2 with PostgreSQL or another robust RDBMS for production deployments.
- **Revise Exception Handling and CSV Data Loading:**  
  Improve error handling and consider batch processing for large CSV files to enhance performance and reliability.
- **Build Release Versions:**  
  Configure Maven to produce release versions (not `-SNAPSHOT`) for production-ready builds.