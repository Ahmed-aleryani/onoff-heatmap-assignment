# Build stage
FROM gradle:8.13-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Create a non-root user
RUN useradd -r -u 1001 -g root appuser
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 