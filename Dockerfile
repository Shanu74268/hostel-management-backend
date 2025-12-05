# Use official OpenJDK 17 image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for caching dependencies)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src src

# Give execution permission to Maven wrapper
RUN chmod +x mvnw

# Build the application without tests
RUN ./mvnw clean package -DskipTests

# Expose default Spring Boot port
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "target/hostel-management-backend-0.0.1-SNAPSHOT.jar"]
