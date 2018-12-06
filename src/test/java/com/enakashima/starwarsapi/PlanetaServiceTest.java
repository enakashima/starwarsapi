package com.enakashima.starwarsapi;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.enakashima.starwarsapi.DTO.MensagemDTO;
import com.enakashima.starwarsapi.DTO.RetornoDTO;
import com.enakashima.starwarsapi.collection.Planeta;
import com.enakashima.starwarsapi.controller.PlanetController;
import com.enakashima.starwarsapi.exception.PlanetaDuplicadoException;
import com.enakashima.starwarsapi.exception.PlanetaNaoEncontradoException;
import com.enakashima.starwarsapi.service.PlanetaService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PlanetController.class, secure=false)
public class PlanetaServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PlanetaService planetaService;

	private Planeta planetaTatooine;
	private Planeta planetaAlderaan;
	private RetornoDTO<Planeta> retornoAdicionarPlaneta;

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
		
		retornoAdicionarPlaneta = new RetornoDTO<>(planetaTatooine);
	}

	@Test
	public void quandoAdicionarUmNovoPlaneta() throws Exception {
		
		Mockito.when(planetaService.adicionar(Mockito.any())).thenReturn(retornoAdicionarPlaneta);

		String planeta = "{\"nome\": \"Tatooine\",\"clima\": \"Árido\",\"terreno\": \"Deserto\"}";
		String retornoEsperado = "{\"retorno\": {\"id\": \"5c091d24fdabf6433e42e5c7\",\"nome\": \"Tatooine\",\"clima\": \"Árido\",\"terreno\": \"Deserto\",\"aparicoesFilmes\": 5},\"mensagens\": []}";
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/starwars/planeta/adicionar")
				.accept(MediaType.APPLICATION_JSON).content(planeta)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		
		JSONAssert.assertEquals(retornoEsperado, response.getContentAsString(), false);

		assertEquals("/api/starwars/planeta/5c091d24fdabf6433e42e5c7",
				response.getHeader(HttpHeaders.LOCATION));
	}
	
	@Test
	public void quandoAdicionarUmPlanetaRepetido() throws Exception {
		
		Mockito.when(planetaService.adicionar(Mockito.any())).thenThrow(new PlanetaDuplicadoException("Adicionado este planeta já foi - Mestre YODA"));

		String planeta = "{\"nome\": \"Tatooine\",\"clima\": \"Árido\",\"terreno\": \"Deserto\"}";
		String retornoEsperado = "{\"retorno\": null,\"mensagens\": [{\"tipo\": \"ERROR\",\"mensagem\": \"Adicionado este planeta já foi - Mestre YODA\"}]}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/starwars/planeta/adicionar")
				.accept(MediaType.APPLICATION_JSON).content(planeta)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		
		JSONAssert.assertEquals(retornoEsperado, response.getContentAsString(), false);
	}
	
	@Test
	public void quandoBuscarPlanetaIndividualmente() throws Exception {
		
		RetornoDTO<Planeta> retornoBuscaPlaneta = new RetornoDTO<>(planetaTatooine);
		
		Mockito.when(planetaService.buscarPlanetaPorId(Mockito.anyString())).thenReturn(retornoBuscaPlaneta);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/starwars/planeta/5c08ff45fdabf63fa22b7a0c")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
		
		String retornoEsperado = "{\"retorno\": {\"id\": \"5c091d24fdabf6433e42e5c7\",\"nome\": \"Tatooine\",\"clima\": \"Árido\",\"terreno\": \"Deserto\",\"aparicoesFilmes\": 5},\"mensagens\": []}";
		
		JSONAssert.assertEquals(retornoEsperado, response.getContentAsString(), false);
	}
	
	@Test
	public void quandoNaoEncontrarPlanetaIndividualmente() throws Exception {
		
		Mockito.when(planetaService.buscarPlanetaPorId(Mockito.anyString())).thenThrow(new PlanetaNaoEncontradoException("O planeta 5c091d24fdabf6433e42e5 encontrado não foi - Mestre YODA"));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/starwars/planeta/5c08ff45fdabf63fa22b7a0c")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		
		String retornoEsperado = "{\"retorno\": null,\"mensagens\": [{\"tipo\": \"ERROR\",\"mensagem\": \"O planeta 5c091d24fdabf6433e42e5 encontrado não foi - Mestre YODA\"}]}";
		
		JSONAssert.assertEquals(retornoEsperado, response.getContentAsString(), false);
	}
		
	@Test
	public void quandoBuscarPlanetasComFiltro() throws Exception {
		
		List<Planeta> planetas = new ArrayList<>();
		planetas.add(planetaTatooine);
		RetornoDTO<List<Planeta>> retornoBuscaPlaneta = new RetornoDTO<>(planetas);
		
		Mockito.when(planetaService.buscarPlaneta(Mockito.anyString())).thenReturn(retornoBuscaPlaneta);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/starwars/planeta/buscar?nome=tatooine")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
		
		String retornoEsperado = "{\"retorno\": [{\"id\": \"5c091d24fdabf6433e42e5c7\",\"nome\": \"Tatooine\",\"clima\": \"Árido\",\"terreno\": \"Deserto\",\"aparicoesFilmes\": 5}],\"mensagens\": [{\"tipo\": \"INFO\",\"mensagem\": \"Encontramos 1 planeta(s) - Que a força esteja com você!\"}]}";
		
		System.out.println(response.getContentAsString());
		JSONAssert.assertEquals(retornoEsperado, response.getContentAsString(), false);
	}
	
	@Test
	public void quandoBuscarPlanetasSemFiltro() throws Exception {
		
		List<Planeta> planetas = new ArrayList<>();
		planetas.add(planetaTatooine);
		planetas.add(planetaAlderaan);
		RetornoDTO<List<Planeta>> retornoBuscaPlaneta = new RetornoDTO<>(planetas);
		
		Mockito.when(planetaService.buscarPlaneta(Mockito.isNull())).thenReturn(retornoBuscaPlaneta);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/starwars/planeta/buscar")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
		
		String retornoEsperado = "{\"retorno\": [{\"id\": \"5c091d24fdabf6433e42e5c7\",\"nome\": \"Tatooine\",\"clima\": \"Árido\",\"terreno\": \"Deserto\",\"aparicoesFilmes\": 5},{\"id\": \"5c0938e2fdabf648d1f61db1\",\"nome\": \"Alderaan\",\"clima\": \"Temperado\",\"terreno\": \"Campo, Montanha\",\"aparicoesFilmes\": 2}],\"mensagens\": [{\"tipo\": \"INFO\",\"mensagem\": \"Encontramos 2 planeta(s) - Que a força esteja com você!\"}]}";
		
		JSONAssert.assertEquals(retornoEsperado, response.getContentAsString(), false);
	}
	
	@Test
	public void quandoRemoverPlaneta() throws Exception {
		
		RetornoDTO<Planeta> retorno = new RetornoDTO<>();
		retorno.addMensagem(new MensagemDTO(MensagemDTO.INFO, "Planeta destruído com sucesso!"));
		
		Mockito.when(planetaService.remover(Mockito.anyString())).thenReturn(retorno);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete("/api/starwars/planeta/remover/5c0938e2fdabf648d1f61db1");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
		
		String retornoEsperado = "{\"retorno\": null,\"mensagens\": [{\"tipo\": \"INFO\",\"mensagem\": \"Planeta destruído com sucesso!\"}]}";
		
		JSONAssert.assertEquals(retornoEsperado, response.getContentAsString(), false);
	}
	
	@Test
	public void quandoTentarRemoverPlanetaInexistente() throws Exception {
		
		Mockito.when(planetaService.remover(Mockito.anyString())).thenThrow(new PlanetaNaoEncontradoException("Planeta não encontrado, parece que a estrela da morte destruiu esse planeta antes de você!"));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete("/api/starwars/planeta/remover/5c0938e2fdabf648d1f61db1");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		
		String retornoEsperado = "{\"retorno\": null,\"mensagens\": [{\"tipo\": \"ERROR\",\"mensagem\": \"Planeta não encontrado, parece que a estrela da morte destruiu esse planeta antes de você!\"}]}";
		
		JSONAssert.assertEquals(retornoEsperado, response.getContentAsString(), false);
	}
}
