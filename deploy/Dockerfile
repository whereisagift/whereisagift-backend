# Stage 1: Build
FROM gradle:7.5.1-jdk17 AS build

WORKDIR /app

# Copy Gradle files and project source code
COPY ../build.gradle ../settings.gradle ./
COPY ../src ./src

# Build the project and generate the JAR file
RUN gradle build -x test

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim AS runtime

WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/your-app.jar ./app.jar

# Set the environment variables (optional)
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Expose the port the app will run on
EXPOSE 8080

# Run the Java application
CMD ["java", "-jar", "/app/app.jar"]
