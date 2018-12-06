package com.enakashima.starwarsapi.exception;

public class SwapiException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public SwapiException() {
		super();
	}

	public SwapiException(String message, Throwable cause) {
		super(message, cause);
	}

	public SwapiException(String message) {
		super(message);
	}

}
