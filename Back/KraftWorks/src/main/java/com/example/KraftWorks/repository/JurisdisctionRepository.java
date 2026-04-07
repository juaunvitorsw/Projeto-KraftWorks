package com.example.KraftWorks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.KraftWorks.model.Jurisdiction;
import java.util.Optional;

public interface JurisdisctionRepository extends JpaRepository<Jurisdiction, Long> {

    Optional<Jurisdiction> findByOcdJurisdictionId(String ocdJurisdictionId);
}