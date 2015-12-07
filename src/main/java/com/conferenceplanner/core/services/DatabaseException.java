package com.conferenceplanner.core.services;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String databaseMessage) {
        super(databaseMessage);
    }
}
