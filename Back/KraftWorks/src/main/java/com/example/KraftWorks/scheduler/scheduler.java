package com.example.KraftWorks.scheduler;

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
    @Scheduled(cron = "0 0 20 * * *") // todo dia às 02:00
    public void executarSyncDiaria() {
        service.syncPeople();
    }
}