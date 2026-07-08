package team.jit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.jit.config.security.JwtService;
import team.jit.dto.MockTokensResponse;
import team.jit.dto.TokenResponse;

/**
 * Authentication controller — provides pre-built mock JWTs for the three sample roles.
 *
 * This is intentionally simplified for workshop / learning purposes:
 * there is no real login or credential validation.  In production you would
 * verify a username + password (or OAuth2 flow) before issuing a token.
 *
 * Endpoints:
 *  GET  /auth/mock-tokens          → returns all three tokens at once
 *  GET  /auth/token/{role}         → returns a token for the requested role
 *                                     accepted values: customer | operator | admin
 *
 * Role → permission matrix
 * ┌──────────────┬────────────────────────────────────────────────────────────┐
 * │ ROLE_CUSTOMER│ GET  /api/employees/**   GET  /api/companies/**            │
 * ├──────────────┼────────────────────────────────────────────────────────────┤
 * │ ROLE_OPERATOR│ CUSTOMER rights +                                          │
 * │              │ POST/PUT/PATCH /api/employees/**                           │
 * │              │ POST/PUT/PATCH /api/companies/**                           │
 * ├──────────────┼────────────────────────────────────────────────────────────┤
 * │ ROLE_ADMIN   │ OPERATOR rights + DELETE on everything                     │
 * └──────────────┴────────────────────────────────────────────────────────────┘
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Returns three mock tokens (one per role) in a single response.
     * Handy for quickly exploring all permission levels.
     *
     * Example:
     * <pre>
     *   GET http://localhost:8080/auth/mock-tokens
     * </pre>
     */
    @GetMapping("/mock-tokens")
    public ResponseEntity<MockTokensResponse> mockTokens() {
        String customerToken = jwtService.generateToken("alice",    "ROLE_CUSTOMER");
        String operatorToken = jwtService.generateToken("bob",      "ROLE_OPERATOR");
        String adminToken    = jwtService.generateToken("charlie",  "ROLE_ADMIN");

        return ResponseEntity.ok(new MockTokensResponse(customerToken, operatorToken, adminToken));
    }

    /**
     * Returns a single mock token for the requested role.
     *
     * @param role  one of: {@code customer}, {@code operator}, {@code admin}
     *
     * Example:
     * <pre>
     *   GET http://localhost:8080/auth/token/admin
     * </pre>
     */
    @GetMapping("/token/{role}")
    public ResponseEntity<TokenResponse> tokenForRole(@PathVariable String role) {
        String springRole = switch (role.toLowerCase()) {
            case "customer" -> "ROLE_CUSTOMER";
            case "operator" -> "ROLE_OPERATOR";
            case "admin"    -> "ROLE_ADMIN";
            default -> throw new IllegalArgumentException(
                    "Unknown role '" + role + "'. Use: customer | operator | admin");
        };

        String username = switch (role.toLowerCase()) {
            case "customer" -> "alice";
            case "operator" -> "bob";
            default         -> "charlie";
        };

        String token = jwtService.generateToken(username, springRole);
        return ResponseEntity.ok(new TokenResponse(username, springRole, token));
    }

    /** Simple handler so Spring returns a clean JSON error instead of a 500. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRole(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

