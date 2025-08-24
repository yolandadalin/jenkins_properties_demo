plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
    id("war")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

// dependencies {
// 	implementation("org.springframework.boot:spring-boot-starter-web")
//     providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
//     implementation("org.apache.tomcat.embed:tomcat-embed-jasper") // Add for JSP support
//     testImplementation("org.springframework.boot:spring-boot-starter-test")
// }

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "ch.qos.logback")
    }
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    providedRuntime("org.apache.tomcat.embed:tomcat-embed-jasper")  // 改為 providedRuntime
    providedCompile("jakarta.servlet:jakarta.servlet-api:6.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.war {
    enabled = true
    archiveFileName.set("hello.war")
    manifest {
        attributes["Spring-Profiles-Active"] = project.findProperty("envProfile") ?: "sit"
    }
}