# Multi-stage build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy the jar file
COPY target/learning-pipelines-0.0.1-SNAPSHOT.jar app.jar

# Extract the layers
RUN java -Djarmode=layertools -jar app.jar extract

# Final stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy layers from builder
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

# Run as non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

