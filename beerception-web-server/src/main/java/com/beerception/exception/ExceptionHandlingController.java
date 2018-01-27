package com.beerception.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Class containing exception handling for controllers throughout application.
 * 
 * @author Miloš Ranđelović
 *
 */
@ControllerAdvice
public class ExceptionHandlingController {

	@ExceptionHandler(DoubleUploadException.class)
	public ResponseEntity<ExceptionResponse> doubleUpload(DoubleUploadException ex) {
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("Double upload");
		response.setErrorMessage(ex.getMessage());

		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceConflictException.class)
	public ResponseEntity<ExceptionResponse> resourceConflict(ResourceConflictException ex) {
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("Conflict");
		response.setErrorMessage(ex.getMessage());

		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ExceptionResponse> constraintViolation(ConstraintViolationException ex) {
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("Constraint violation");
		
		String errorMessage = "";
		for(ConstraintViolation<?> x : ex.getConstraintViolations())
		{
			errorMessage = errorMessage + x.getMessage() + ". ";
		}
		response.setErrorMessage(errorMessage);

		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> methodArgumentNotValid(MethodArgumentNotValidException ex) {
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("Method argument not valid");
		
		String errorMessage = "";
		for (ObjectError x : ex.getBindingResult().getAllErrors())
		{
			errorMessage = errorMessage + x.getDefaultMessage() + ". ";
		}
		response.setErrorMessage(errorMessage);

		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}
}
