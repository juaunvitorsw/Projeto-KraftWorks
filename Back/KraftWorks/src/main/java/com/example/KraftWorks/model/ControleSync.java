package com.example.KraftWorks.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class ControleSync {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate dataExecucao;
    private String tipoSync;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public LocalDate getDataExecucao() {
		return dataExecucao;
	}
	public void setDataExecucao(LocalDate dataExecucao) {
		this.dataExecucao = dataExecucao;
	}
	public String getTipoSync() {
		return tipoSync;
	}
	public void setTipoSync(String tipoSync) {
		this.tipoSync = tipoSync;
	}
    
}
