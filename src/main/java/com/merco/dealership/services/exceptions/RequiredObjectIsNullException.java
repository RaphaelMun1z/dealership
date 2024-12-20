package com.merco.dealership.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectIsNullException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RequiredObjectIsNullException() {
		super("Required object is null.");
	}

	public RequiredObjectIsNullException(String e) {
		super(e);
	}

}
