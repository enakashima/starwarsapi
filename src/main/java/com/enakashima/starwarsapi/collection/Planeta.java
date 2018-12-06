package com.enakashima.starwarsapi.collection;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Planeta {
	
    private String id;
    @Indexed(unique = true)
    @NotEmpty(message="Informe o nome do planeta")
    private String nome;
    @NotEmpty(message="Informe o clima do planeta")
    private String clima;
    @NotEmpty(message="Informe o terreno do planeta")
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clima == null) ? 0 : clima.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((terreno == null) ? 0 : terreno.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Planeta other = (Planeta) obj;
		if (clima == null) {
			if (other.clima != null)
				return false;
		} else if (!clima.equals(other.clima))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (terreno == null) {
			if (other.terreno != null)
				return false;
		} else if (!terreno.equals(other.terreno))
			return false;
		return true;
	}
}
