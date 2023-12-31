plugins {
	java
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	groovy
}

group = "com.sinam"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	//jpa
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	//spring web
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	//swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

	//mapper
	implementation("org.mapstruct:mapstruct-jdk8:1.3.0.Final")
	annotationProcessor("org.mapstruct:mapstruct-jdk8:1.5.3.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.3.0.Final")

	//postgresql
	runtimeOnly("org.postgresql:postgresql")

	//liquibase
	implementation("org.liquibase:liquibase-core")

	//security
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	//validation
	implementation("org.springframework.boot:spring-boot-starter-validation")

	//spock
	testImplementation ("org.spockframework:spock-spring:2.3-groovy-4.0")

	//random create object
	testImplementation("io.github.benas:random-beans:3.9.0")



}

tasks.withType<Test> {
	useJUnitPlatform()
}
