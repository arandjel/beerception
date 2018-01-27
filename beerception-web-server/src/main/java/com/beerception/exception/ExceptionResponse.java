package com.beerception.exception;

/**
 * Class presenting exception response. Class is a Bean, meaning that it contains
 * private fields with getters and setters as well as default constructor.
 * 
 * @author Miloš Ranđelović
 *
 */
public class ExceptionResponse {

	private String errorCode;
	private String errorMessage;

	public ExceptionResponse() {
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
