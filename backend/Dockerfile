# Use lightweight JDK image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy JAR from Maven build
COPY backend/target/*.jar app.jar

# copy root .env file into container
COPY ../.env /app/.env

# Expose the application port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","/app/app.jar"]
