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
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

/** ① 讀取 Jenkins 傳進來的 -PenvProfile，預設 sit */
val envProfile: String = (findProperty("envProfile") as String?) ?: "sit"

/** ② 產生屬性檔，內容像：envName=uat  */
val generateEnvProps by tasks.registering {
    val outDir = layout.buildDirectory.dir("generated-resources")
    outputs.dir(outDir)

    doLast {
        val dir = outDir.get().asFile
        dir.mkdirs()
        file("${dir}/app-env.properties").writeText("envName=${envProfile}\n")
    }
}

/** ③ 把產生出來的資料夾，納入 main resources，打進 WAR 的 WEB-INF/classes */
sourceSets {
    named("main") {
        resources {
            srcDir(layout.buildDirectory.dir("generated-resources"))
        }
    }
}

/** ④ 確保處理資源前，先產生屬性檔 */
tasks.named<ProcessResources>("processResources") {
    dependsOn(generateEnvProps)
}

/** ⑤ WAR 設定：移除 manifest 裡的 Spring-Profiles-Active（對外部 JBoss 無效） */
tasks.war {
    enabled = true
    archiveFileName.set("hello.war")
    manifest {
        // 移除這行：attributes["Spring-Profiles-Active"] = project.findProperty("envProfile") ?: "sit"
    }
}