plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}


dependencies {
	implementation("org.projectlombok:lombok:1.18.36")
//	implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:11.1.0")
	implementation("com.graphql-java-kickstart:graphql-java-tools:11.1.0")
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation
	implementation("org.springframework.boot:spring-boot-starter-validation:2.7.4")
	// https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api
//	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
