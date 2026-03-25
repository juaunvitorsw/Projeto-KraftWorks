package com.example.KraftWorks.service;

import com.example.KraftWorks.model.Person;
import com.example.KraftWorks.repository.PersonRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonQueryService {

    private final PersonRepository repository;

    public PersonQueryService(PersonRepository repository) {
        this.repository = repository;
    }

    @Cacheable("peopleAll")
    public List<Person> listarTodos() {
        System.out.println("BUSCOU NO BANCO");
        return repository.findAll();
    }
    
    @Cacheable(value = "peopleByEstado", key = "#estado")
    public List<Person> listarPorEstado(String estado) {
        return repository.findByEstadoIgnoreCase(estado);
    }
    
    @Cacheable(value = "peopleByPartido", key = "#partido")
    public List<Person> listarPorPartido(String partido) {
        return repository.findByPartidoIgnoreCase(partido);
    }
    
    @Cacheable(value = "peopleByEstadoPartido", key = "#estado + '_' + #partido")
    public List<Person> listarPorEstadoEPartido(String estado, String partido) {
        return repository.findByEstadoIgnoreCaseAndPartidoIgnoreCase(estado, partido);
    }
}