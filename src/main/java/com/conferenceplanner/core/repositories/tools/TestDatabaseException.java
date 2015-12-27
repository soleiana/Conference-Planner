package com.conferenceplanner.core.repositories.tools;


public class TestDatabaseException extends Exception{

    public TestDatabaseException(String message) {
        super(message);
    }

    public TestDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestDatabaseException(Throwable cause) {
        super(cause);
    }
}
