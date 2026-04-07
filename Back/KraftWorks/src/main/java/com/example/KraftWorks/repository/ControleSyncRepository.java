package com.example.KraftWorks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.KraftWorks.model.ControleSync;
import java.time.LocalDate;

public interface ControleSyncRepository extends JpaRepository<ControleSync, Long> {

    boolean existsByDataExecucaoAndTipoSync(LocalDate dataExecucao, String tipoSync);
}