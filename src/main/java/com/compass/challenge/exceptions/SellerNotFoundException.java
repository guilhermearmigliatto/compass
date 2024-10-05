package com.compass.challenge.exceptions;

public class SellerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5364761458696718237L;

	public SellerNotFoundException(String message) {
		super(message);
	}
	
	public SellerNotFoundException(String message, Throwable error) {
		super(message, error);
	}
}
