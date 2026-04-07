package com.example.KraftWorks.controller;

import org.springframework.web.bind.annotation.*;

import com.example.KraftWorks.model.Jurisdiction;
import com.example.KraftWorks.model.Person;
import com.example.KraftWorks.repository.PersonRepository;
import com.example.KraftWorks.service.*;


import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonRepository repository;
    private final PersonService service;
    private final PersonQueryService queryService;

    public PersonController(PersonRepository repository, PersonService service, PersonQueryService queryService) {
        this.repository = repository;
        this.service = service;
        this.queryService = queryService;
    }

    @GetMapping
    public List<Person> listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String partido
    ) {
        if (estado != null && partido != null) {
            return queryService.listarPorEstadoEPartido(estado, partido);
        } else if (estado != null) {
            return queryService.listarPorEstado(estado);
        } else if (partido != null) {
            return queryService.listarPorPartido(partido);
        }
        return queryService.listarTodos();    }

    @PostMapping
    public Person salvar(@RequestBody Person person) {
        return repository.save(person);
    }
   
    @GetMapping("/sync")
    public String sync() {
        service.syncJurisdiction();
        service.syncPeople();
        return "Sincronização iniciada";
    }
    
    @GetMapping("/jurisdictions")
    public List<Jurisdiction> listarEstados(){
    	return queryService.listarTodosEstados();
    }
    
}