package com.cassi.desafiocassi.configuration.exceptions;

public class ObjectNotFoundException extends RuntimeException {
	/**
	 * construtor de ObjectNotFoundException que recebe uma mensagem do tipo String
	 * @param msg mensagem
	 */
	public ObjectNotFoundException (String msg) {
		super(msg);
	}
}