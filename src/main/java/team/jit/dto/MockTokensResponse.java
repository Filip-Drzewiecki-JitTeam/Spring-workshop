package team.jit.dto;

/**
 * Response body returned by GET /auth/mock-tokens
 *
 * Contains one pre-generated JWT for each sample role.
 * Use each token in the Authorization header:
 *   Authorization: Bearer <token>
 */
public record MockTokensResponse(
        String customerToken,   // alice  — ROLE_CUSTOMER (read-only)
        String operatorToken,   // bob    — ROLE_OPERATOR (read + write)
        String adminToken       // charlie— ROLE_ADMIN    (full access)
) {}

