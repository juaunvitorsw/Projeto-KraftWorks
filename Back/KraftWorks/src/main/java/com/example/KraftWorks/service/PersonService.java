package com.example.KraftWorks.service;

import com.example.KraftWorks.model.ControleSync;
import com.example.KraftWorks.model.Jurisdiction;
import com.example.KraftWorks.model.Person;
import com.example.KraftWorks.repository.ControleSyncRepository;
import com.example.KraftWorks.repository.JurisdisctionRepository;
import com.example.KraftWorks.repository.PersonRepository;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

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
    private static final String TIPO_SYNC_PEOPLE = "PEOPLE";
    private static final String TIPO_SYNC_JURISDICTION = "JURISDICTION";

    private final PersonRepository repository;
    private final JurisdisctionRepository jurisdictionRepository;
    private final JsonMapper jsonMapper;
    private final RestTemplate http = new RestTemplate();
    private final ControleSyncRepository controleRepository;
    @Value("${api.url}")
    private String url;

    @Value("${api.key}")
    private String apiKey;
    
    public PersonService(PersonRepository repository, JurisdisctionRepository jurisdictionRepository,
            JsonMapper jsonMapper, ControleSyncRepository controleRepository) {
        this.repository = repository;
        this.jurisdictionRepository = jurisdictionRepository;
        this.jsonMapper = jsonMapper;
        this.controleRepository = controleRepository;
    }

    @Transactional
    public String syncPeople() {
    	
        LocalDate hoje = LocalDate.now();

        // Verifica se já rodou hoje para sync de people
        boolean jaExecutou = controleRepository.existsByDataExecucaoAndTipoSync(hoje, TIPO_SYNC_PEOPLE);

        if (jaExecutou) {
            System.out.println("⛔ Sync já executado hoje. Cancelando...");
            return "⛔ Sync já executado hoje. Cancelando...";
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);

        List<Jurisdiction> jurisdictions = jurisdictionRepository.findAll();
        if (jurisdictions.isEmpty()) {
            return "Nenhuma jurisdiction cadastrada. Execute syncJurisdiction antes.";
        }
        
        String lastBody = "";

        for (Jurisdiction jur : jurisdictions) {
            String nomeJur = jur.getNome();
            if (nomeJur == null || nomeJur.isBlank()) {
                continue;
            }

            try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            ResponseEntity<String> response = null;
            
            try {
        	   response = http.exchange(
                           url + "?jurisdiction=" + nomeJur,
                           HttpMethod.GET,
                           new HttpEntity<>(headers),
                           String.class
                   );
			} catch (Exception e) {
			    System.out.println("Erro ao chamar API: " + e.getMessage());
			    continue;
			}
         

            String body = response.getBody();
            if (body == null || body.isBlank()) {
                continue;
            }

            lastBody = body;
            JsonNode root = jsonMapper.readTree(body);
            JsonNode results = root.path("results");
            if (!results.isArray()) {
                continue;
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
        }

        ControleSync controle = new ControleSync();
        controle.setDataExecucao(hoje);
        controle.setTipoSync(TIPO_SYNC_PEOPLE);

        controleRepository.save(controle);

        System.out.println("✅ Sync finalizado e registrado.");

        return lastBody.isEmpty() ? "" : lastBody;
    }

    @Transactional
    public String syncJurisdiction() {

        LocalDate hoje = LocalDate.now();

        boolean jaExecutou = controleRepository.existsByDataExecucaoAndTipoSync(hoje, TIPO_SYNC_JURISDICTION);

        if (jaExecutou) {
            System.out.println("⛔ Sync já executado hoje. Cancelando...");
            return "⛔ Sync já executado hoje. Cancelando...";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);

        String jurisdictionsUrl = URI.create(url).resolve("/jurisdictions").toString();

        ResponseEntity<String> response = http.exchange(
                jurisdictionsUrl,
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

            Jurisdiction j = jurisdictionRepository.findByOcdJurisdictionId(ocdId).orElseGet(Jurisdiction::new);
            j.setOcdJurisdictionId(ocdId);
            j.setNome(n.path("name").asString());

            jurisdictionRepository.save(j);
        }

        ControleSync controle = new ControleSync();
        controle.setDataExecucao(hoje);
        controle.setTipoSync(TIPO_SYNC_JURISDICTION);

        controleRepository.save(controle);

        System.out.println("✅ Sync finalizado e registrado.");

        return body;
    }
}
