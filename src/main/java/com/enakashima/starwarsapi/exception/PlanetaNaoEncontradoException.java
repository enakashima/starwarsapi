package com.enakashima.starwarsapi.exception;

public class PlanetaNaoEncontradoException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public PlanetaNaoEncontradoException() {
		super();
	}

	public PlanetaNaoEncontradoException(String message, Throwable cause) {
		super(message, cause);
	}

	public PlanetaNaoEncontradoException(String message) {
		super(message);
	}

}
