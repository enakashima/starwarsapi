package com.enakashima.starwarsapi.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enakashima.starwarsapi.DTO.MensagemDTO;
import com.enakashima.starwarsapi.DTO.RetornoDTO;
import com.enakashima.starwarsapi.collection.Planeta;
import com.enakashima.starwarsapi.exception.PlanetaDuplicadoException;
import com.enakashima.starwarsapi.exception.PlanetaNaoEncontradoException;
import com.enakashima.starwarsapi.exception.SwapiException;
import com.enakashima.starwarsapi.service.PlanetaService;

@RestController
@RequestMapping("${api.base.uri}")
public class PlanetController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PlanetaService planetaService;
	
	@Value("${api.base.uri}")
    public String apiBaseUri;
	
	@PostMapping(path="adicionar", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RetornoDTO<Planeta>> adicionar(@RequestBody Planeta planeta) {
		
		try {
			
			RetornoDTO<Planeta> retorno = planetaService.adicionar(planeta);
			
			URI localizacaoPlaneta = new URI(apiBaseUri+"/"+retorno.getRetorno().getId());
			
			return ResponseEntity.created(localizacaoPlaneta).body(retorno);
		} catch (SwapiException | PlanetaDuplicadoException | URISyntaxException e ) {
			logger.error(e.getMessage(), e);
			RetornoDTO<Planeta> error = new RetornoDTO<>();
			error.addMensagem(new MensagemDTO(MensagemDTO.ERROR, e.getMessage()));
			return ResponseEntity.badRequest().body(error);
		}
    }
	
	@GetMapping(path="/buscar", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<RetornoDTO<List<Planeta>>> buscar(@RequestParam(value = "nome", required=false) String nome) {
			
		RetornoDTO<List<Planeta>> retorno = planetaService.buscarPlaneta(nome);
		
		if(retorno.getRetorno().isEmpty()) {
			retorno.addMensagem(new MensagemDTO(MensagemDTO.INFO, "Nenhum Planeta foi encontrado, jovem padawan!"));
		}else {
			String mensagem = String.format("Encontramos %d planeta(s) - Que a força esteja com você!", retorno.getRetorno().size());
			retorno.addMensagem(new MensagemDTO(MensagemDTO.INFO, mensagem));
		}
		
		return ResponseEntity.accepted().body(retorno);
	}
	
	@GetMapping(path="/{idPlaneta}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<RetornoDTO<Planeta>> buscarPorId(@PathVariable(name = "idPlaneta", required = true) String idPlaneta) {
		
		try {
			
			RetornoDTO<Planeta> retorno = planetaService.buscarPlanetaPorId(idPlaneta);
			return ResponseEntity.accepted().body(retorno);
		} catch (PlanetaNaoEncontradoException e) {
			RetornoDTO<Planeta> error = new RetornoDTO<>();
			error.addMensagem(new MensagemDTO(MensagemDTO.ERROR, e.getMessage()));
			return ResponseEntity.badRequest().body(error);
		} 
	}
	
	@DeleteMapping(path="remover/{idPlaneta}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<RetornoDTO<Planeta>> remover(@PathVariable(name = "idPlaneta", required = true) String idPlaneta) {
		
		RetornoDTO<Planeta> retorno;
		try {
			retorno = planetaService.remover(idPlaneta);
			return ResponseEntity.accepted().body(retorno); 
		} catch (PlanetaNaoEncontradoException e) {
			RetornoDTO<Planeta> error = new RetornoDTO<>();
			error.addMensagem(new MensagemDTO(MensagemDTO.ERROR, e.getMessage()));
			return ResponseEntity.badRequest().body(error);
		}
	}
	
}
