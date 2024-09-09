package com.cassi.desafiocassi.configuration.exceptions;

public class DataIntegrityException extends RuntimeException {
	/**
	 * construtor do DataIntegrityException que recebe uma mensagem do tipo String
	 * @param msg mensagem
	 */
	public DataIntegrityException(String msg) {
		super(msg);
	}
}