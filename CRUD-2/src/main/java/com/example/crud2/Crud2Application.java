package com.example.crud2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Crud2Application {

    public static void main(String[] args) {
        SpringApplication.run(Crud2Application.class, args);
    }

}
