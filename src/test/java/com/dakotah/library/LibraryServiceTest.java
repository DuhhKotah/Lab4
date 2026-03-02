package com.dakotah.library;

import com.dakotah.library.exceptions.DatabaseFailureException;
import com.dakotah.library.exceptions.EmailFailureException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private EmailProvider emailProvider;

    @Mock
    private ResourceRepository resourceRepository;

    @Test
    void nullUUID_returnsFalse() throws Exception {
        LibraryService service = new LibraryService(emailProvider, resourceRepository);

        boolean result = service.checkoutResource(null, "member@test.com");

        assertFalse(result);
        verifyNoInteractions(resourceRepository, emailProvider);
    }

    @Test
    void resourceNotAvailable_returnsFalse() throws Exception {
        UUID id = UUID.randomUUID();
        LibraryService service = new LibraryService(emailProvider, resourceRepository);

        when(resourceRepository.isResourceAvailable(id)).thenReturn(false);

        boolean result = service.checkoutResource(id, "member@test.com");

        assertFalse(result);
        verify(resourceRepository).isResourceAvailable(id);
        verify(resourceRepository, never()).updateStatus(any(), anyBoolean());
        verifyNoInteractions(emailProvider);
    }

    @Test
    void updateFails_throwsDatabaseFailureException() throws Exception {
        UUID id = UUID.randomUUID();
        LibraryService service = new LibraryService(emailProvider, resourceRepository);

        when(resourceRepository.isResourceAvailable(id)).thenReturn(true);
        when(resourceRepository.updateStatus(id, false)).thenReturn(false);

        assertThrows(DatabaseFailureException.class,
                () -> service.checkoutResource(id, "member@test.com"));

        verifyNoInteractions(emailProvider);
    }

    @Test
    void emailFails_throwsEmailFailureException() throws Exception {
        UUID id = UUID.randomUUID();
        LibraryService service = new LibraryService(emailProvider, resourceRepository);

        when(resourceRepository.isResourceAvailable(id)).thenReturn(true);
        when(resourceRepository.updateStatus(id, false)).thenReturn(true);
        when(emailProvider.sendEmail(eq("member@test.com"), anyString())).thenReturn(false);

        assertThrows(EmailFailureException.class,
                () -> service.checkoutResource(id, "member@test.com"));
    }

    @Test
    void successfulCheckout_returnsTrue() throws Exception {
        UUID id = UUID.randomUUID();
        LibraryService service = new LibraryService(emailProvider, resourceRepository);

        when(resourceRepository.isResourceAvailable(id)).thenReturn(true);
        when(resourceRepository.updateStatus(id, false)).thenReturn(true);
        when(emailProvider.sendEmail(eq("member@test.com"), anyString())).thenReturn(true);

        boolean result = service.checkoutResource(id, "member@test.com");

        assertTrue(result);
    }
}