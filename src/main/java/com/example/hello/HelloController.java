package com.example.hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HelloController {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Value("${app.env:default}")
    private String env;

    @GetMapping("/")
    public String hello(Model model) {
        logger.info("Environment value: {}", env);
        model.addAttribute("env", env);
        return "hello";  // 對應 hello.jsp
    }

}
