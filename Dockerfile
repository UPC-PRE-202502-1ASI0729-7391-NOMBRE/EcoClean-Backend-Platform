# =========================
# 1. Build stage
# =========================
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build JAR
RUN mvn -B -DskipTests clean package

# =========================
# 2. Runtime stage
# =========================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy final Jar from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose port 8080 (default Spring Boot)
EXPOSE 8080

# Start app
ENTRYPOINT ["java", "-jar", "app.jar"]
