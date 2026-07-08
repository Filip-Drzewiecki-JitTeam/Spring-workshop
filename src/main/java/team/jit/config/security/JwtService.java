package team.jit.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * Service responsible for:
 *  - Generating signed JWTs (including mock tokens for sample roles)
 *  - Parsing / validating incoming JWTs
 *
 * Algorithm: HMAC-SHA256 (HS256)
 * Claims layout:
 *   sub   – username / identity
 *   role  – single Spring Security role string, e.g. "ROLE_ADMIN"
 *   iat   – issued-at (epoch seconds)
 *   exp   – expiration (epoch seconds)
 */
@Service
public class JwtService {

    /** HMAC-SHA256 secret – must be ≥ 256 bits (32 chars). Loaded from application.properties. */
    @Value("${jwt.secret}")
    private String secret;

    /** Token lifetime in milliseconds (default 24 h). */
    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    // -------------------------------------------------------------------------
    // Token generation
    // -------------------------------------------------------------------------

    /**
     * Build a signed JWT for the given subject and role.
     *
     * @param username identity that will be stored in the {@code sub} claim
     * @param role     Spring Security role, e.g. "ROLE_ADMIN"
     * @return compact JWT string
     */
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)
                .claims(Map.of("role", role))   // custom claim carrying the authority
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey())
                .compact();
    }

    // -------------------------------------------------------------------------
    // Token parsing / validation
    // -------------------------------------------------------------------------

    /**
     * Extract the {@code sub} claim from a verified token.
     *
     * @throws JwtException if the token is invalid or expired
     */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extract the {@code role} claim from a verified token.
     *
     * @throws JwtException if the token is invalid or expired
     */
    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    /**
     * Return {@code true} when the token signature is valid and the token has not expired.
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey signingKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

