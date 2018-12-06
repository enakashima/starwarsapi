package com.enakashima.starwarsapi.exception;

public class PlanetaDuplicadoException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public PlanetaDuplicadoException() {
		super();
	}

	public PlanetaDuplicadoException(String message, Throwable cause) {
		super(message, cause);
	}

	public PlanetaDuplicadoException(String message) {
		super(message);
	}

}
