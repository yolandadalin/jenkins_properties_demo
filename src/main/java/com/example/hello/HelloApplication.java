package com.example.hello;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class HelloApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		var app = new org.springframework.boot.SpringApplication(HelloApplication.class);
        app.setAdditionalProfiles(com.example.hello.config.EnvSupport.resolveEnv());
        app.run(args);
	}

}
