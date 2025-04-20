# Stage 0: Build JAR
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY gradle gradle
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY src ./src

RUN chmod +x ./gradlew && ./gradlew clean build -x test

# Stage 1: Runtime
FROM eclipse-temurin:17-jre-alpine as runtime

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENV JAVA_OPTS="-Xms128m -Xmx512m -XX:+UseSerialGC"

EXPOSE 3000

# Используем ENTRYPOINT для запуска приложения с переменными окружения
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
