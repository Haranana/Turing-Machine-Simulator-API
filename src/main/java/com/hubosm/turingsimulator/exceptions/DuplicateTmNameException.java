package com.hubosm.turingsimulator.exceptions;

public class DuplicateTmNameException extends RuntimeException {
    private final Long existingId;
    private final String name;

    public DuplicateTmNameException(Long existingId, String name) {
        super("Machine with name '" + name + "' already exists for this user");
        this.existingId = existingId;
        this.name = name;
    }
    public Long getExistingId() { return existingId; }
    public String getName() { return name; }
}