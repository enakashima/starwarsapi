package com.enakashima.starwarsapi.helper;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.enakashima.starwarsapi.exception.SwapiException;

@Component
public class SwapiHelper {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${swapi.base.uri}")
    public String swapiBaseUri;
	@Value("${user.agent}")
    public String userAgent;
	
	public Integer buscarQuantidadeAparicoesEmFilmes(String nomePlaneta) throws SwapiException {
		
		ResponseEntity<String> resposta = chamarSwapi(String.format(swapiBaseUri, nomePlaneta));
		
		validarRetornoApi(resposta);
		
		return extrairQuantidadeAparicoesRetorno(resposta.getBody());
	}

	private ResponseEntity<String> chamarSwapi(String uri) {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		headers.add("user-agent", userAgent);
		HttpEntity<String> httpEntity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
		
		return response;
	}
	
	private void validarRetornoApi(ResponseEntity<String> response) throws SwapiException {
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new SwapiException("A api swapi não retornou conforme esperado");
		}
	}
	
	private Integer extrairQuantidadeAparicoesRetorno(String resposta) throws SwapiException {
		
		Integer quantidadeAparicoes = 0;
		
		try {
			
			JSONObject respostaJson = new JSONObject(resposta);
		
			if(respostaJson.getInt("count") > 0) {
				JSONObject planetas = (JSONObject) respostaJson.getJSONArray("results").get(0);
				JSONArray filmes = planetas.getJSONArray("films");
				quantidadeAparicoes = filmes.length();
			}
		} catch (JSONException e) {
			throw new SwapiException("Erro ao converter o retorno da api swapi", e.getCause());
		}
		
		return quantidadeAparicoes;
	}
}
