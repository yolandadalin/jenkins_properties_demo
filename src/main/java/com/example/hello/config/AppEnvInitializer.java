package com.example.hello.config;

import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 啟動時讀取 classpath:app-env.properties（由 Gradle 產生）
 * 讀不到就退回 JVM system property，再退回 default
 */
@Configuration
@PropertySource(value = "classpath:app-env.properties", ignoreResourceNotFound = true)
public class AppEnvInitializer {

    @Bean
    ApplicationRunner appEnvRunner(
            ServletContext servletContext,
            @Value("${envName:#{null}}") String envFromProps
    ) {
        return args -> {
            String env = envFromProps;
            if (env == null || env.isBlank()) {
                env = System.getProperty("APP_ENV");
            }
            if (env == null || env.isBlank()) {
                env = "default";
            }
            servletContext.setAttribute("APP_ENV", env);
        };
    }
}
