package com.example.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.example.hello.config.EnvSupport;

@SpringBootApplication
public class HelloApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
    var app = new SpringApplication(HelloApplication.class);
    app.setAdditionalProfiles(EnvSupport.resolveEnv()); // 內建 Tomcat 跑時也啟用對應 profile
    app.run(args);
  }

}
