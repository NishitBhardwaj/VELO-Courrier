# Stage 1: Build the application using maven
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# Copy only the POM first to cache dependencies layers
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code and compile
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Create a minimal JRE runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy built flat jar from builder
COPY --from=builder /build/target/*.jar app.jar

# Expose standard port
EXPOSE 8080

# Run with container optimizations
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
