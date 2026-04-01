package com.example.KraftWorks.service;

import com.example.KraftWorks.model.ControleSync;
import com.example.KraftWorks.model.Person;
import com.example.KraftWorks.repository.ControleSyncRepository;
import com.example.KraftWorks.repository.PersonRepository;

import jakarta.annotation.PostConstruct;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Service
public class PersonService {

    private final PersonRepository repository;
    private final JsonMapper jsonMapper;
    private final RestTemplate http = new RestTemplate();
    private final ControleSyncRepository controleRepository;
    @Value("${api.url}")
    private String url;

    @Value("${api.key}")
    private String apiKey;
    
    public PersonService(PersonRepository repository, JsonMapper jsonMapper,ControleSyncRepository controleRepository) {
        this.repository = repository;
        this.jsonMapper = jsonMapper;
        this.controleRepository = controleRepository;
    }

    @Transactional
    public String syncPeople() {
    	
        LocalDate hoje = LocalDate.now();

        // 🔍 Verifica se já rodou hoje
        boolean jaExecutou = controleRepository.existsByDataExecucao(hoje);

        if (jaExecutou) {
            System.out.println("⛔ Sync já executado hoje. Cancelando...");
            return "⛔ Sync já executado hoje. Cancelando...";
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);

        ResponseEntity<String> response = http.exchange(
        		url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        String body = response.getBody();
        if (body == null || body.isBlank()) {
            return "";
        }

        JsonNode root = jsonMapper.readTree(body);
        JsonNode results = root.path("results");
        if (!results.isArray()) {
            return body;
        }

        for (int i = 0; i < results.size(); i++) {
            JsonNode n = results.get(i);
            String ocdId = n.path("id").asString();
            if (ocdId.isEmpty()) {
                continue;
            }

            Person p = repository.findByOcdPersonId(ocdId).orElseGet(Person::new);
            p.setOcdPersonId(ocdId);
            p.setNome(n.path("name").asString());
            p.setPartido(n.path("party").asString());
            String foto = n.path("image").asString();
            p.setFotoUrl(foto.isEmpty() ? null : foto);

            JsonNode jurisdiction = n.path("jurisdiction");
            if (!jurisdiction.isMissingNode()) {
                p.setEstado(jurisdiction.path("name").asString());
            }

            JsonNode role = n.path("current_role");
            if (!role.isMissingNode()) {
                p.setCargo(role.path("title").asString());
            }

            repository.save(p);
        }
        
        ControleSync controle = new ControleSync();
        controle.setDataExecucao(hoje);

        controleRepository.save(controle);

        System.out.println("✅ Sync finalizado e registrado.");

        return body;
    }
}
