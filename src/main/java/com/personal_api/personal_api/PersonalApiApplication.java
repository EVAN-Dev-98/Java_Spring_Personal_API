package com.personal_api.personal_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PersonalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalApiApplication.class, args);

    }

    @GetMapping("/")
    public String home() {
        return String.format("Welcome to personal_api!");
    }


}
