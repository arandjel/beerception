package com.beerception.exception;

/**
 * Exception used with resource conflict.
 * Example, when user tries to register with the email that already exists.
 * 
 * @author Miloš Ranđelović
 *
 */
public class ResourceConflictException extends RuntimeException {

	private static final long serialVersionUID = 1791564636123821405L;

	private Integer resourceId;

	public ResourceConflictException(Integer resourceId, String message) {
		super(message);
		this.setResourceId(resourceId);
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
}
