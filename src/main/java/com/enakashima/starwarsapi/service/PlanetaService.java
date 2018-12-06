package com.enakashima.starwarsapi.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.enakashima.starwarsapi.DTO.MensagemDTO;
import com.enakashima.starwarsapi.DTO.RetornoDTO;
import com.enakashima.starwarsapi.collection.Planeta;
import com.enakashima.starwarsapi.exception.PlanetaDuplicadoException;
import com.enakashima.starwarsapi.exception.PlanetaNaoEncontradoException;
import com.enakashima.starwarsapi.exception.SwapiException;
import com.enakashima.starwarsapi.helper.SwapiHelper;
import com.enakashima.starwarsapi.repository.PlanetaRepository;

@Service
public class PlanetaService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PlanetaRepository planetaRepository;
	
	@Autowired
	private SwapiHelper swapiHelper;
	
	public RetornoDTO<Planeta> adicionar(Planeta planeta) throws SwapiException, PlanetaDuplicadoException {
		
		Integer quantidadeAparicoes = swapiHelper.buscarQuantidadeAparicoesEmFilmes(planeta.getNome());
		planeta.setAparicoesFilmes(quantidadeAparicoes);
		
		logger.info(planeta.toString());
		try {
			return new RetornoDTO<Planeta>(planetaRepository.save(planeta)); 
		}catch (DuplicateKeyException e) {
			throw new PlanetaDuplicadoException("Adicionado este planeta já foi - Mestre YODA", e.getCause());
		}
	}

	public RetornoDTO<Planeta> buscarPlanetaPorId(String idPlaneta) throws PlanetaNaoEncontradoException {
		
		Optional<Planeta> planeta = planetaRepository.findById(idPlaneta);
		
		if(planeta.isPresent()) {
			return new RetornoDTO<Planeta>(planeta.get());
		}else {
			throw new PlanetaNaoEncontradoException(String.format("O planeta %s encontrado não foi - Mestre YODA", idPlaneta));
		}
	}

	public RetornoDTO<List<Planeta>> buscarPlaneta(String nome) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("nome").is(nome));
		
		List<Planeta> resultados = null;
		if(!StringUtils.isEmpty(nome)) {
			resultados = planetaRepository.findByNomeContainingIgnoreCase(nome);
		}else {
			resultados = planetaRepository.findAll();
		}
		
		return new RetornoDTO<>(resultados);
	}

	public RetornoDTO<Planeta> remover(String idPlaneta) throws PlanetaNaoEncontradoException {
		
		Optional<Planeta> planeta = planetaRepository.findById(idPlaneta);
		
		if(planeta.isPresent()) {
			planetaRepository.delete(planeta.get());
			
			RetornoDTO<Planeta> resposta = new RetornoDTO<>();
			resposta.addMensagem(new MensagemDTO(MensagemDTO.INFO, "Planeta destruído com sucesso!"));
			
			return resposta; 
		}else {
			throw new PlanetaNaoEncontradoException("Planeta não encontrado, parece que a estrela da morte destruiu esse planeta antes de você!");
		}
	}
	
}
