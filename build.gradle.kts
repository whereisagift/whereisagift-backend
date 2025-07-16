import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.whereisagift"
version = "0.0.1-SNAPSHOT"


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}


dependencies {
    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    // MapStruct
    compileOnly("org.mapstruct:mapstruct:1.6.3")
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    // Commons Codec
    implementation("commons-codec:commons-codec:1.18.0")

    // Spring Boot Starters (версии подтягиваются из spring-boot-dependencies)
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Sentry SDK
    implementation("io.sentry:sentry-spring-boot-starter-jakarta:8.16.0")
    implementation("io.sentry:sentry-logback:8.16.0")

    // GraphQL Extended Scalars
    implementation("com.graphql-java:graphql-java-extended-scalars:24.0")

    // Database
    runtimeOnly("org.postgresql:postgresql")

    // Тесты
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-webflux")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Devtools
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<ProcessResources> {
    filteringCharset = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs = listOf("-Dfile.encoding=UTF-8")
}


tasks.named<BootRun>("bootRun") {
    jvmArgs = listOf(
        "-Dfile.encoding=UTF-8",
        // ваш JDWP-порт
        "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000"
    )
}
