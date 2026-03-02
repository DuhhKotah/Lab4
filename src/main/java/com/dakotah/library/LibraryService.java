package com.dakotah.library;

import com.dakotah.library.exceptions.DatabaseFailureException;
import com.dakotah.library.exceptions.EmailFailureException;

import java.util.UUID;

public class LibraryService {

    private final EmailProvider emailProvider;
    private final ResourceRepository resourceRepository;

    public LibraryService(EmailProvider emailProvider,
                          ResourceRepository resourceRepository) {
        this.emailProvider = emailProvider;
        this.resourceRepository = resourceRepository;
    }

    public boolean checkoutResource(UUID resourceId, String memberEmail)
            throws DatabaseFailureException, EmailFailureException {

        if (resourceId == null) return false;

        if (!resourceRepository.isResourceAvailable(resourceId)) {
            return false;
        }

        boolean statusUpdated =
                resourceRepository.updateStatus(resourceId, false);

        if (!statusUpdated) {
            throw new DatabaseFailureException("Could not check out item.");
        }

        boolean emailSent =
                emailProvider.sendEmail(memberEmail,
                        "Resource ID: " + resourceId + " checked out.");

        if (!emailSent) {
            throw new EmailFailureException("Could not send email.");
        }

        return true;
    }
}