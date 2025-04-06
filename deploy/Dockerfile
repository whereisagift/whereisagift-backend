# Stage 1: Build
FROM gradle:7.5.1-jdk17 AS build

WORKDIR /app

COPY ../gradlew ../build.gradle.kts ./
COPY ../gradle ./gradle
COPY ../src ./src

RUN gradle build -x test

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim AS runtime

WORKDIR /app

COPY --from=build /app/build/libs/your-app.jar ./app.jar

ENV JAVA_OPTS="-Xms512m -Xmx1024m"

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
