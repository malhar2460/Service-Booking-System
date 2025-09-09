# Use lightweight JDK image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy JAR from Maven build
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 8082

# Run the app
ENTRYPOINT ["java","-jar","/app/app.jar"]
