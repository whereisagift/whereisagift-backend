# Используем официальный образ OpenJDK 17
FROM eclipse-temurin:17-jdk AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы проекта
COPY . .

# Собираем приложение с помощью Gradle
RUN ./gradlew build -x test

# Используем минимальный образ для запуска
FROM eclipse-temurin:17-jre

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR из предыдущего этапа
COPY --from=build /app/build/libs/*.jar app.jar

# Запускаем приложение
CMD ["java", "-jar", "app.jar"]

