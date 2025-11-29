package com.hubosm.turingsimulator.exceptions;

public class ExpirationDatePassedException extends RuntimeException {
    public ExpirationDatePassedException(String message) {
        super(message);
    }
}
