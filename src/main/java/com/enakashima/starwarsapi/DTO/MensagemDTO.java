package com.enakashima.starwarsapi.DTO;

public class MensagemDTO {
	
	public static final String INFO = "INFO";
	public static final String ERROR = "ERROR";
	
	String tipo;
	String mensagem;

	public MensagemDTO(String tipo, String mensagem) {
		super();
		this.tipo = tipo;
		this.mensagem = mensagem;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
