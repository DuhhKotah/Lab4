package com.dakotah.library;

import com.dakotah.library.exceptions.EmailFailureException;

public interface EmailProvider {
    boolean sendEmail(String recipient, String message)
            throws EmailFailureException;
}