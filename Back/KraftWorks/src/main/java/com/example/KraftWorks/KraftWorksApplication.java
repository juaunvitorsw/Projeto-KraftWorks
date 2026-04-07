package com.example.KraftWorks;

import com.example.KraftWorks.scheduler.scheduler;
import com.example.KraftWorks.service.PersonService;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

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

    private final PersonService service;

	KraftWorksApplication( PersonService service) {
        this.service = service;
	}

	public static void main(String[] args) {
        SpringApplication.run(KraftWorksApplication.class, args);
    }
	
    @PostConstruct
    public void init() {
        System.out.println("🚀 Verificando execução ao iniciar...");
        
        CompletableFuture.runAsync(() -> {
            service.syncJurisdiction();
            service.syncPeople();
        });
    }
}