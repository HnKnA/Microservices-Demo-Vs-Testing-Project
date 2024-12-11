# ====== Stage 1: Build the entire project ======
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the entire source code into the container
COPY . .

# Build all modules, skip tests for faster builds
RUN mvn clean package -DskipTests

# ====== Stage 2: Create a lightweight runtime image ======
FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app

# Copy only the inventory service JAR from the build stage
COPY --from=build /app/inventory/target/inventory-0.0.1-SNAPSHOT.jar inventory.jar

# Expose the internal port used by the inventory service
EXPOSE 8082

# Run the inventory service when the container starts
CMD ["java", "-jar", "inventory.jar"]
