plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.whereisagift"
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
	implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:11.1.0")
	implementation("com.graphql-java:graphql-java-extended-scalars:22.0")
	implementation("com.graphql-java-kickstart:graphql-java-tools:11.1.0")
	implementation("org.springframework.boot:spring-boot-starter-validation:2.7.4")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	developmentOnly("org.springframework.boot:spring-boot-devtools") // devtools для разработки
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootRun {
	jvmArgs = listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000")
}
