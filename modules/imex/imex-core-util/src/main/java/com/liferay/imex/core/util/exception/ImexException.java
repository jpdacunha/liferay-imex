package com.liferay.imex.core.util.exception;

public class ImexException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImexException(String string) {
		super(string);
	}

	public ImexException(Exception e) {
		super(e);
	}

}
