package com.dakotah.library.exceptions;

public class EmailFailureException extends Exception {

    public EmailFailureException(String message) {
        super("Email Failed! " + message);
    }
}