package com.example.KraftWorks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.KraftWorks.model.ControleSync;
import java.time.LocalDate;
import java.util.Date;

public interface ControleSyncRepository extends JpaRepository<ControleSync, Long> {

    boolean existsByDataExecucao(LocalDate dataExecucao);
}