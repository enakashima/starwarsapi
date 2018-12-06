package com.enakashima.starwarsapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import com.enakashima.starwarsapi.DTO.RetornoDTO;
import com.enakashima.starwarsapi.collection.Planeta;
import com.enakashima.starwarsapi.exception.PlanetaDuplicadoException;
import com.enakashima.starwarsapi.exception.PlanetaNaoEncontradoException;
import com.enakashima.starwarsapi.exception.SwapiException;
import com.enakashima.starwarsapi.helper.SwapiHelper;
import com.enakashima.starwarsapi.repository.PlanetaRepository;
import com.enakashima.starwarsapi.service.PlanetaService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PlanetaService.class)
public class PlanetaServiceTest {

	@MockBean
	private PlanetaRepository planetaRepository;
	
	@MockBean
	private SwapiHelper swapiHelper;

	private Planeta planetaTatooine;
	private Planeta planetaAlderaan;
	
	@Autowired
	PlanetaService planetaService;
	
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	@Before
	public void setUp() {
		planetaTatooine = new Planeta();
		planetaTatooine.setId("5c091d24fdabf6433e42e5c7");
		planetaTatooine.setNome("Tatooine");
		planetaTatooine.setClima("Árido");
		planetaTatooine.setTerreno("Deserto");
		planetaTatooine.setAparicoesFilmes(5);
		
		planetaAlderaan = new Planeta();
		planetaAlderaan.setId("5c0938e2fdabf648d1f61db1");
		planetaAlderaan.setNome("Alderaan");
		planetaAlderaan.setClima("Temperado");
		planetaAlderaan.setTerreno("Campo, Montanha");
		planetaAlderaan.setAparicoesFilmes(2);
	}
	
	@Test
	public void quandoIncluirComSucesso() throws SwapiException, PlanetaDuplicadoException {
		
		Planeta planetaASalvar = new Planeta();
		planetaASalvar.setNome("Tatooine");
		planetaASalvar.setClima("Árido");
		planetaASalvar.setTerreno("Deserto");
		
		Planeta planetaSalvo = new Planeta();
		planetaSalvo.setId("5c091d24fdabf6433e42e5c7");
		planetaSalvo.setNome("Tatooine");
		planetaSalvo.setClima("Árido");
		planetaSalvo.setTerreno("Deserto");
		planetaSalvo.setAparicoesFilmes(5);
		
		Mockito.when(planetaRepository.save(Mockito.any())).thenReturn(planetaSalvo);
		Mockito.when(swapiHelper.buscarQuantidadeAparicoesEmFilmes(Mockito.any())).thenReturn(5);
		
		RetornoDTO<Planeta> retorno = planetaService.adicionar(planetaASalvar);
		
		System.out.println(retorno.getRetorno().toString());
		
		assertEquals(planetaSalvo, retorno.getRetorno());
		assertNotNull(retorno.getRetorno().getId());
		assertNotNull(retorno.getRetorno().getAparicoesFilmes());
	}
	
	@Test
	public void quandoIncluirDuplicado() throws SwapiException, PlanetaDuplicadoException {
		
		Planeta planetaASalvar = new Planeta();
		planetaASalvar.setNome("Tatooine");
		planetaASalvar.setClima("Árido");
		planetaASalvar.setTerreno("Deserto");
		
		Mockito.when(planetaRepository.save(Mockito.any())).thenThrow(new DuplicateKeyException(null));
		Mockito.when(swapiHelper.buscarQuantidadeAparicoesEmFilmes(Mockito.any())).thenReturn(5);
		
		exceptionRule.expect(PlanetaDuplicadoException.class);
	    exceptionRule.expectMessage("Adicionado este planeta já foi - Mestre YODA");
	    
		planetaService.adicionar(planetaASalvar);
	}
	
	@Test
	public void quandoPlanetaEncontrado() throws PlanetaNaoEncontradoException  {
		
		Planeta planeta = new Planeta();
		planeta.setId("5c091d24fdabf6433e42e5c7");
		planeta.setNome("Tatooine");
		planeta.setClima("Árido");
		planeta.setTerreno("Deserto");
		planeta.setAparicoesFilmes(5);
		
		Mockito.when(planetaRepository.findById(Mockito.anyString())).thenReturn(Optional.of(planeta));
		
		RetornoDTO<Planeta> resultadoBusca = planetaService.buscarPlanetaPorId(planeta.getId());
		
		assertEquals(planeta, resultadoBusca.getRetorno());
	}
	
	@Test
	public void quandoPlanetaNaoEncontrado() throws PlanetaNaoEncontradoException  {
		
		Planeta planeta = new Planeta();
		planeta.setId("5c091d24fdabf6433e42e5c7");
		
		Mockito.when(planetaRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		
		exceptionRule.expect(PlanetaNaoEncontradoException.class);
	    exceptionRule.expectMessage(String.format("O planeta %s encontrado não foi - Mestre YODA", planeta.getId()));
	    
	    planetaService.buscarPlanetaPorId(planeta.getId());
	}
	
	@Test
	public void quandoBuscarTodosOsPlanetas() throws PlanetaNaoEncontradoException  {
		
		List<Planeta> resultadoFake = new ArrayList<>();
		resultadoFake.add(planetaAlderaan);
		resultadoFake.add(planetaTatooine);
		
		Mockito.when(planetaRepository.findAll()).thenReturn(resultadoFake);
		
	    RetornoDTO<List<Planeta>> resultado = planetaService.buscarPlaneta(null);
	    
	    assertNotNull(resultado);
	    assertNotNull(resultado.getRetorno());
	    assertEquals(2, resultado.getRetorno().size());
	}
	
	@Test
	public void quandoBuscarComFiltro() throws PlanetaNaoEncontradoException  {
		
		List<Planeta> resultadoFake = new ArrayList<>();
		resultadoFake.add(planetaAlderaan);
		
		Mockito.when(planetaRepository.findByNomeContainingIgnoreCase(Mockito.anyString())).thenReturn(resultadoFake);
		
	    RetornoDTO<List<Planeta>> resultado = planetaService.buscarPlaneta("Alderaan");
	    
	    assertNotNull(resultado);
	    assertNotNull(resultado.getRetorno());
	    assertEquals(1, resultado.getRetorno().size());
	}
	
	@Test
	public void quandoDeletarPlaneta() throws PlanetaNaoEncontradoException  {
		
		Mockito.when(planetaRepository.findById(Mockito.anyString())).thenReturn(Optional.of(planetaAlderaan));
		Mockito.doNothing().when(planetaRepository).delete(planetaAlderaan);;
		
	    RetornoDTO<Planeta> resultado = planetaService.remover(planetaAlderaan.getId());
	    
	    assertNotNull(resultado);
	    assertNotNull(resultado.getMensagens());
	    assertEquals(1, resultado.getMensagens().size());
	}
	
	@Test
	public void quandoTentarDeletarPlanetaInexistente() throws PlanetaNaoEncontradoException  {
		
		Mockito.when(planetaRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		
	    exceptionRule.expect(PlanetaNaoEncontradoException.class);
	    exceptionRule.expectMessage("Planeta não encontrado, parece que a estrela da morte destruiu esse planeta antes de você!");
	    
	    planetaService.remover("12376123152735172537162");
	}
}
