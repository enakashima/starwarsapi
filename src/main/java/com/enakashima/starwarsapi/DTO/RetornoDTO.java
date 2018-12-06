package com.enakashima.starwarsapi.DTO;

import java.util.ArrayList;
import java.util.List;

public class RetornoDTO<T> {
	
	private T retorno;
	private List<MensagemDTO> mensagens = new ArrayList<>();
	
	public RetornoDTO() {
		super();
	}
	
	public RetornoDTO(T retorno) {
		super();
		this.retorno = retorno;
	}
	
	public T getRetorno() {
		return retorno;
	}
	public void setRetorno(T retorno) {
		this.retorno = retorno;
	}
	public List<MensagemDTO> getMensagens() {
		return mensagens;
	}
	public void addMensagem(MensagemDTO mensagem) {
		this.mensagens.add(mensagem);
	}
}
