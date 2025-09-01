package com.example.hello;

import com.example.hello.config.EnvSupport;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
    return app.sources(HelloApplication.class)          // 你的 @SpringBootApplication 類別
              .profiles(EnvSupport.resolveEnv());      // ★ 提前啟用 uat/sit/prod
  }
}
