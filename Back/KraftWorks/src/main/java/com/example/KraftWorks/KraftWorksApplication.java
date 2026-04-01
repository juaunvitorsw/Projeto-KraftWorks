package com.example.KraftWorks;

import com.example.KraftWorks.scheduler.scheduler;
import com.example.KraftWorks.service.PersonService;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

import org.springframework.cache.annotation.EnableCaching;

import java.security.Provider.Service;

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
		Dotenv dotenv = Dotenv.configure()
		        .directory("C:/")
		        .load();


        System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));

        System.setProperty("API_URL", dotenv.get("API_URL"));
        System.setProperty("API_KEY", dotenv.get("API_KEY"));
        
        System.setProperty("REDIS_HOST", dotenv.get("REDIS_HOST"));
        System.setProperty("REDIS_PORT", dotenv.get("REDIS_PORT"));

        
        
        SpringApplication.run(KraftWorksApplication.class, args);
    }
	
    @PostConstruct
    public void init() {
        System.out.println("🚀 Verificando execução ao iniciar...");
        service.syncPeople();
    }
}