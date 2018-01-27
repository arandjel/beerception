package com.beerception.exception;

/**
 * Exception used with resource conflict.
 * Example, when user tries to register with the email that already exists.
 * 
 * @author Miloš Ranđelović
 *
 */
public class DoubleUploadException extends RuntimeException {

	private static final long serialVersionUID = 1791564636123821405L;
	
	public DoubleUploadException(String message) {
		super(message);
	}
}
