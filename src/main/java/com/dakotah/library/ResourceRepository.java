package com.dakotah.library;

import com.dakotah.library.exceptions.DatabaseFailureException;
import java.util.UUID;

public interface ResourceRepository {

    boolean isResourceAvailable(UUID resource);

    boolean updateStatus(UUID resource, boolean available)
            throws DatabaseFailureException;
}