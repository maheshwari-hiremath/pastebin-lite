# Use Maven + Java 17 image
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy Maven project
COPY pom.xml .
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# Second stage: run the jar
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy built jar from previous stage
COPY --from=build /app/target/pastebin-lite-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
