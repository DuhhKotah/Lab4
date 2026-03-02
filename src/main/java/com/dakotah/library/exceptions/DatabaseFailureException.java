package com.dakotah.library.exceptions;

public class DatabaseFailureException extends Exception {

    public DatabaseFailureException(String message) {
        super("Database Failed! " + message);
    }
}