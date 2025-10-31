plugins {
	java
	id("org.springframework.boot") version "3.5.7"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.notes"
version = "0.0.1-SNAPSHOT"
description = "Demo project for notes"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.projectlombok:lombok:1.18.40")
	annotationProcessor("org.projectlombok:lombok:1.18.42")
	implementation("io.jsonwebtoken:jjwt-impl:0.13.0")
	implementation("io.jsonwebtoken:jjwt-jackson:0.13.0")
	implementation("io.jsonwebtoken:jjwt-api:0.13.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:mongodb:1.21.3")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
