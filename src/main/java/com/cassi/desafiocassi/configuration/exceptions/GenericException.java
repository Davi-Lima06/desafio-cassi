package com.cassi.desafiocassi.configuration.exceptions;

public class GenericException extends RuntimeException {

	/**
	 * construtor de GenericException que recebe uma mensagem do tipo String
	 * @param msg mensagem
	 */
	public GenericException(String msg) {
		super(msg);
	}
}