package com.example.KraftWorks;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@ComponentScan(basePackages = "com.example.KraftWorks")
public class KraftWorksApplication {

    public static void main(String[] args) {
        SpringApplication.run(KraftWorksApplication.class, args);
    }
}