# Базовый образ с JDK 17
FROM eclipse-temurin:17-jdk

# Копируем файл сборки (JAR)
COPY build/libs/whereIsAGift-0.0.1-SNAPSHOT.jar app.jar

# Указываем, какой порт будет использовать приложение
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]

