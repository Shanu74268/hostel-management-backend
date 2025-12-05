# Use official OpenJDK 17 image
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn
COPY src ./src

RUN ./mvnw clean package -DskipTests

ENV PORT 8080
EXPOSE $PORT

ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

CMD java -jar target/*.jar --server.port=$PORT
