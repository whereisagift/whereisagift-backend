# Базовый образ с JDK 17
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY gradle gradle
COPY gradlew build.gradle.kts ./
COPY src ./src

RUN ./gradlew clean build -x test

CMD ["./gradlew", "bootRun"]

