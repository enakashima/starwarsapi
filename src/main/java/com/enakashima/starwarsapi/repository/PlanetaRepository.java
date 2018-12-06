package com.enakashima.starwarsapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.enakashima.starwarsapi.collection.Planeta;

public interface PlanetaRepository extends MongoRepository<Planeta, String> {
	
	List<Planeta> findByNomeContainingIgnoreCase(String nome);
}
