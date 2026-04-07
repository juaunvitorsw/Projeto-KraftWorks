package com.example.KraftWorks.scheduler;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.KraftWorks.service.PersonService;

@Component
public class scheduler {

    private final PersonService service;

    public scheduler(PersonService service) {
        this.service = service;
    }

    // @Scheduled(fixedRate = 60000) // a cada 1 minuto
    @Scheduled(cron = "0 0 02 * * *") // todo dia às 02:00
    public void executarSyncDiaria() {
    	CompletableFuture.runAsync(() -> {
            service.syncJurisdiction();
            service.syncPeople();
        });
    }
}