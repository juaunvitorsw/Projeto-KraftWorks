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

    @Cacheable("people")
    public List<Person> listarTodos() {
        System.out.println("BUSCOU NO BANCO");
        return repository.findAll();
    }
}