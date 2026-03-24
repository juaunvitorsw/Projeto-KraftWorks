package com.example.KraftWorks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.KraftWorks")
public class KraftWorksApplication {

    public static void main(String[] args) {
        SpringApplication.run(KraftWorksApplication.class, args);
    }
}