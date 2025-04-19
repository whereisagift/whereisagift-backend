# Stage 0: Build JAR
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY gradle gradle
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY src ./src

ARG SPRING_DATASOURCE_URL
ENV SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL
ARG SERVER_PORT
ENV SERVER_PORT=$SERVER_PORT

RUN chmod +x ./gradlew && ./gradlew clean build -x test

# Stage 1: Runtime
FROM eclipse-temurin:17-jdk-jammy as runtime

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENV JAVA_OPTS="-Xms512m -Xmx1024m"

EXPOSE 8080

CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
