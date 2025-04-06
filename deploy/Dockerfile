# Stage 1: Create the runtime image
FROM openjdk:17-jdk-slim AS runtime

WORKDIR /app

# Копируем собранный JAR файл в контейнер
COPY build/libs/*.jar ./app.jar

# Устанавливаем переменные окружения (по желанию)
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Открываем порт, на котором будет работать приложение
EXPOSE 8080

# Запускаем Java приложение
CMD ["java", "-jar", "/app/app.jar"]
