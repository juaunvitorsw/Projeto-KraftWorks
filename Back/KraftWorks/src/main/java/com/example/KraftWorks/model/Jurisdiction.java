package com.example.KraftWorks.model;

import java.io.Serializable;

import jakarta.persistence.*;
@Entity
public class Jurisdiction implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ocdJurisdictionId;

    private String nome;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOcdJurisdictionId() {
		return ocdJurisdictionId;
	}
	public void setOcdJurisdictionId(String ocdJurisdictionId) {
		this.ocdJurisdictionId = ocdJurisdictionId;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}