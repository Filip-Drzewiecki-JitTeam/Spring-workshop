package team.jit.dto;

/**
 * Response body returned by GET /auth/token/{role}
 *
 * Fields:
 *  - username  : the mock user identity embedded in the JWT
 *  - role      : Spring Security role string (e.g. "ROLE_ADMIN")
 *  - token     : compact signed JWT — pass this as "Authorization: Bearer <token>"
 */
public record TokenResponse(String username, String role, String token) {}

