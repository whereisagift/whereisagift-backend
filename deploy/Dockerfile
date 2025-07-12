# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

COPY gradle gradle
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY src ./src

RUN chmod +x ./gradlew && ./gradlew clean build -x test --no-daemon

FROM gcr.io/distroless/java17-debian11:nonroot AS runtime
WORKDIR /app

# Copy JAR from builder
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]