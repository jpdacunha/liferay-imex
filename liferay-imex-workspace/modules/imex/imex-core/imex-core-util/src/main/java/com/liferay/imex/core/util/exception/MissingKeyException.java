package com.liferay.imex.core.util.exception;

public class MissingKeyException extends ImexException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6869901324377808088L;

	public MissingKeyException(Exception e) {
		super(e);
	}
	
	public MissingKeyException(String string) {
		super(string);
	}

}
