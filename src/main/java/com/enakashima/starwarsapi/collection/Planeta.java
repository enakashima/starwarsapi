package com.enakashima.starwarsapi.collection;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Planeta {
	
    private String id;
    @Indexed(unique = true)
    private String nome;
    private String clima;
	private String terreno;
	private Integer aparicoesFilmes;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getClima() {
		return clima;
	}
	public void setClima(String clima) {
		this.clima = clima;
	}
	public String getTerreno() {
		return terreno;
	}
	public void setTerreno(String terreno) {
		this.terreno = terreno;
	}
	public Integer getAparicoesFilmes() {
		return aparicoesFilmes;
	}
	public void setAparicoesFilmes(Integer aparicoesFilmes) {
		this.aparicoesFilmes = aparicoesFilmes;
	}
	
	@Override
    public String toString() {
        return String.format(
                "Planeta[id=%s, nome='%s', clima='%s', terreno='%s', aparicoesFilmes='%d']",
                id, nome, clima, terreno, aparicoesFilmes);
    }
}
