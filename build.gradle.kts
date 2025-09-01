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
    toolchain { languageVersion = JavaLanguageVersion.of(17) }
}

repositories { mavenCentral() }

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "ch.qos.logback")
    }
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    providedRuntime("org.apache.tomcat.embed:tomcat-embed-jasper")
    // 建議用 compileOnly 取代 providedCompile（避免被打進 WAR）
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> { useJUnitPlatform() }

/** 讀取 Jenkins 的 -PenvProfile，沒給就預設 sit（本機打包不指定時仍保留所有 profile 檔） */
val envProfile: String? = (findProperty("envProfile") as String?)?.lowercase()

/** ① 產生 app-env.properties：讓 JSP 顯示目前環境用（已經驗證成功的那段保留） */
val generateEnvProps by tasks.registering {
    val outDir = layout.buildDirectory.dir("generated-resources")
    outputs.dir(outDir)
    doLast {
        val dir = outDir.get().asFile
        dir.mkdirs()
        file("${dir}/app-env.properties").writeText("envName=${envProfile ?: "sit"}\n")
    }
}

/** ② 把 generated-resources 納入資源，打進 WAR 的 WEB-INF/classes */
sourceSets {
    named("main") {
        resources { srcDir(layout.buildDirectory.dir("generated-resources")) }
    }
}

/** ③ 資源處理：若指定了 envProfile，就排除其他環境的 application-*.{properties,yml,yaml}  */
tasks.named<ProcessResources>("processResources") {
    dependsOn(generateEnvProps)

    // 只有在 CI（Jenkins）指定了 envProfile 才做裁切；本機沒指定就不動，保留全部
    if (!envProfile.isNullOrBlank()) {
        // 要保留的檔名（根目錄下）
        val keepProps = "application-$envProfile.properties"
        val keepYml   = "application-$envProfile.yml"
        val keepYaml  = "application-$envProfile.yaml"

        // 找出 src/main/resources 根目錄下所有 application-*.{properties,yml,yaml}
        val toExclude = fileTree("src/main/resources") {
            include("application-*.properties", "application-*.yml", "application-*.yaml")
        }.files
         .map { it.name }
         .filter { it != keepProps && it != keepYml && it != keepYaml }

        // 排除不要的（注意：不影響 application.properties）
        exclude(toExclude)
    }
}

/** ④ WAR：不需要再把 Spring-Profiles-Active 寫進 manifest（對外部 JBoss 無效） */
tasks.war {
    enabled = true
    archiveFileName.set("hello.war")
    manifest {
        // 移除舊的：attributes["Spring-Profiles-Active"] = ...
    }
}
