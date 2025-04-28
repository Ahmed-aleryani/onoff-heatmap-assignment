# onoff Heatmap Assignment

A Spring Boot application that provides heatmap data for answer rates, exposing a secure REST endpoint backed by an in-memory H2 database.

---

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Installation](#installation)
3. [Configuration](#configuration)
4. [Running the Application](#running-the-application)
   - [Using Gradle](#using-gradle)
   - [Using Docker](#using-docker)
5. [API Usage](#api-usage)
6. [Testing](#testing)
7. [Mock Data Generation](#mock-data-generation)
8. [Notes](#notes)

---

## Prerequisites
- Java 17 or higher
- Gradle Wrapper (included)
- Docker & Docker Compose (optional, for containerized deployment)

---

## Installation

1. **Clone the repository**  
   ```bash
   git clone https://github.com/Ahmed-aleryani/onoff-heatmap-assignment.git
   cd onoff-heatmap-assignment
   ```

2. **Build the project**  
   ```bash
   ./gradlew build
   ```

---

## Configuration

### Authentication
- Default Basic Auth credentials:
  - Username: `admin`
  - Password: `admin`
- **Override** via environment variables or in `application-*.yml`:
  - `HEATMAP_USER` - username
  - `HEATMAP_PASS` - password

### Spring Profiles & Datasource
Settings live in `/src/main/resources/`:
- `application.yml` (default) (in-memory H2)
- `application-local.yml` (in-memory H2)
- `application-prod.yml` (file-based H2)

### Configuration Files

#### Default Configuration
- [src/main/resources/application.yml](src/main/resources/application.yml)

#### Local Development Configuration
- [src/main/resources/application-local.yml](src/main/resources/application-local.yml)

#### Production Configuration
- [src/main/resources/application-prod.yml](src/main/resources/application-prod.yml)

Activate a profile:
```bash
./gradlew bootRun --args='--spring.profiles.active=<local|prod|delete option to use default>'
# or
java -jar build/libs/heatmap-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

---

## Running the Application

### Using Gradle
```bash
./gradlew bootRun
```  
Application runs on `http://localhost:8080`

### Using Docker

#### Development

```bash
# Build the Docker image
docker build -t heatmap .

# Run the container (foreground mode to see logs)
docker run -p 8080:8080 heatmap

# Or run in background mode
docker run -d -p 8080:8080 heatmap
```

The application will be available at `http://localhost:8080`

#### Production

```bash
# Build the Docker image
docker build -t heatmap .

# Run the container with production profile
docker run -d -p 8080:9090 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:h2:file:./data/heatmap \
  -e SPRING_DATASOURCE_USERNAME=sa \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e SPRING_H2_CONSOLE_ENABLED=true \
  -e HEATMAP_USER=admin \
  -e HEATMAP_PASS=admin \
  -v $(pwd)/data:/app/data \
  heatmap
```

Note: In production mode, the application runs on port 9090 and persists data to a local `data` directory.

---

## API Usage

### Endpoint
```
GET /api/heatmap/answer-rate
```  
**Query Parameters**:
- `dateInput` (required): `YYYY-MM-DD`
- `numberOfShades` (required): 3–10
- `startHour` (optional): 0–23 (default: 0)
- `endHour` (optional): 0–23 (default: 23)

**Response**: JSON array of hourly objects with fields:
- `hour` (Integer)
- `answeredCalls` (Integer)
- `totalCalls` (Integer)
- `rate` (Float, percentage)
- `shade` (String, 1–N)

### Example
```bash
curl -X GET "http://localhost:8080/api/heatmap/answer-rate?dateInput=2025-04-28&numberOfShades=5&startHour=0&endHour=23" \
  -H "Accept: application/json" \
  -u "admin:admin"
```

---

## Testing

Run all tests with Gradle:
```bash
./gradlew test
```  
Generate coverage report:
```bash
./gradlew test jacocoTestReport
```

Test reports: `build/reports/tests/test/index.html`

---

## Mock Data Generation

A Python script generates sample call logs for 3 days:
`src/main/resources/dataGenerator.py`

```bash
cd src/main/resources
python dataGenerator.py
```  
This produces `data.sql` for populating the H2 database.

---

## Notes
- In-memory H2 is used by default for local dev.
- Secure credentials via environment variables—never commit secrets.
- For any issues or questions, please open an issue in the repository.