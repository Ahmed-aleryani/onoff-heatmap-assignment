# onoff heatmap assignment

A Spring Boot application that provides heatmap data for answer rates.

## Prerequisites

- Java 17 or higher
- Gradle

## Running the Application

### Using Gradle Wrapper

```bash
# Clone the repository
git clone https://github.com/Ahmed-aleryani/onoff-heatmap-assignment
cd heatmap-assignment

# Run the application
./gradlew bootRun
```

### Building and Running the JAR

```bash
# Build the application
./gradlew build

# Run the JAR
java -jar build/libs/heatmap-0.0.1-SNAPSHOT.jar
```

## Running Tests

```bash
# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests com.onoffapp.heatmap.controller.HeatmapControllerTest

# Run tests with coverage report
./gradlew test jacocoTestReport

# Skip tests during build
./gradlew build -x test
```

Test results can be found in `build/reports/tests/test/index.html`

## Authentication

The application uses Basic Authentication. Default credentials:

- Username: `admin`
- Password: `admin`

You can override these defaults by setting environment variables:
- `HEATMAP_USER` - to change the username
- `HEATMAP_PASS` - to change the password

## API Endpoints

### Get Answer Rate Data

```
GET /api/heatmap/answer-rate
```

Parameters:
- `dateInput`: Date in format yyyy-MM-dd
- `numberOfShades`: Number of shades for the heatmap
- `startHour`: Starting hour (0-23)
- `endHour`: Ending hour (0-23)

### Example Curl Command

```bash
curl -X GET "http://localhost:8080/api/heatmap/answer-rate?dateInput=2025-04-28&numberOfShades=5&startHour=0&endHour=23" \
  -H "Accept: application/json" \
  -u "admin:admin"
```

### Import to Postman

To import the curl command into Postman:

1. In Postman, click on **Import** in the sidebar
2. Paste the curl command above into the text box
3. Choose one of the following options:
   - **Import Into Collection** - to save the request in a new or existing collection
   - **Import Without Saving** - to open as a new request without saving

## Configuration

### Default confiurations (application.yml)

- [src/main/resources/application.yaml](src/main/resources/application.yml)

### Local Development Configuration (application-local.yml)

- [src/main/resources/application-local.yml](src/main/resources/application-local.yml)

### Production Configuration (application-prod.yml)

- [src/main/resources/application-prod.yml](src/main/resources/application-prod.yml)

To use a specific profile, run the application with:

```bash
./gradlew bootRun --args='--spring.profiles.active=<env name local | prod or leave empty for default>'
# or
java -jar build/libs/heatmap-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Notes

- The application uses an H2 database which is in-memory by default for local development and file-based for production.
- For production use, ensure to set secure credentials via environment variables and never commit them to version control.
- The heatmap data is accessed through a RESTful API with Basic Authentication (username & password).

## Mock Data Generation

The application includes a Python script for generating test data:

- [src/main/resources/dataGenerator.py](src/main/resources/dataGenerator.py)

This script generates sample call log data for testing the heatmap functionality. It creates records with:
- Random call durations
- Various call statuses (ANSWER, MISSED, ERROR)
- Different users and phone numbers
- Timestamps across multiple days

To run the script:

```bash
cd src/main/resources
python dataGenerator.py
```

The script will generate a `data.sql` file that can be used to populate the database with test data. 