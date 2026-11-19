package com.eventostec.api.exceptions;

import com.eventostec.api.adapters.inbound.exceptions.ApiExceptionHandler;
import com.eventostec.api.adapters.inbound.exceptions.ProblemDetails;
import com.eventostec.api.application.InvalidAdminKeyException;
import com.eventostec.api.domain.event.EventNotFoundException;
import com.eventostec.api.domain.image.ImageUploadException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiExceptionHandlerTest {

    private ApiExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new ApiExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/event/123");
    }

    @Test
    void test_shouldReturnNotFoundForEventNotFoundException() {
        UUID eventId = UUID.randomUUID();
        ResponseEntity<ProblemDetails> response = handler.handleNotFound(new EventNotFoundException(eventId), request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().status());
        assertEquals("/api/event/123", response.getBody().instance());
    }

    @Test
    void test_shouldReturnForbiddenForInvalidAdminKeyException() {
        ResponseEntity<ProblemDetails> response = handler.handleForbidden(new InvalidAdminKeyException(), request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getBody().status());
    }

    @Test
    void test_shouldReturnBadGatewayForImageUploadException() {
        ResponseEntity<ProblemDetails> response = handler.handleUpload(new ImageUploadException("falha", new RuntimeException()), request);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertEquals(HttpStatus.BAD_GATEWAY.value(), response.getBody().status());
    }

    @Test
    void test_shouldReturnNotFoundForEmptyResultDataAccessException() {
        ResponseEntity<ProblemDetails> response = handler.handleEmptyResult(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void test_shouldReturnBadRequestForUncheckedIOException() {
        ResponseEntity<ProblemDetails> response = handler.handleUncheckedIO(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void test_shouldReturnInternalServerErrorAsCatchAll() {
        ResponseEntity<ProblemDetails> response = handler.handleUnexpected(new RuntimeException("bug inesperado"), request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status());
    }
}
