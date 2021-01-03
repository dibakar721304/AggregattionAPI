package com.dibakar.aggregation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingQueryParameterException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public MissingQueryParameterException(String message) {
		super(message);
	} 

}