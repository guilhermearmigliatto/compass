package com.compass.challenge.exceptions;

public class ChargeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5364761458696718237L;

    public ChargeNotFoundException(String message) {
        super(message);
    }

    public ChargeNotFoundException(String message, Throwable error) {
        super(message, error);
    }
}
