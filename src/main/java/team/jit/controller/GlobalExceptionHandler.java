package team.jit.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

/**
 * Centralized exception → HTTP status mapping for all REST controllers.
 *
 * Why needed:
 *   Spring MVC wraps unhandled exceptions in a ServletException and returns 500.
 *   This handler intercepts well-known domain exceptions and converts them to
 *   meaningful HTTP responses before the response is committed.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * {@code jakarta.persistence.EntityNotFoundException} is thrown by the service
     * layer when a requested entity does not exist in the database.
     * Maps to HTTP 404 Not Found.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public Map<String, Object> handleEntityNotFound(EntityNotFoundException ex) {
        return Map.of(
                "status",    404,
                "error",     "Not Found",
                "message",   ex.getMessage(),
                "timestamp", Instant.now().toString()
        );
    }
}

