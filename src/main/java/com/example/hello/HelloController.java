package com.example.hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @Value("${app.env:default}")
    private String env;

    @GetMapping("/")
    public String hello(Model model) {
        model.addAttribute("env", env);
        return "hello";  // 對應 hello.jsp
    }

}
