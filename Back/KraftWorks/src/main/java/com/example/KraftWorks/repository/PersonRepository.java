package com.example.KraftWorks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.KraftWorks.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByOcdPersonId(String ocdPersonId);

    List<Person> findByEstado(String estado);

    List<Person> findByPartido(String partido);

    List<Person> findByEstadoAndPartido(String estado, String partido);
}