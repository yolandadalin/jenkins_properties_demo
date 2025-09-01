package com.example.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.hello.config.EnvSupport;

@SpringBootApplication
public class HelloApplication {

	public static void main(String[] args) {
    var app = new SpringApplication(HelloApplication.class);
    app.setAdditionalProfiles(EnvSupport.resolveEnv()); // 內建 Tomcat 一致
    app.run(args);
  }

}
