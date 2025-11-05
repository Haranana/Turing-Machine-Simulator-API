package com.hubosm.turingsimulator.exceptions;

public class StorageLimitException extends RuntimeException {
    public StorageLimitException(String message) {
        super(message);
    }
}
