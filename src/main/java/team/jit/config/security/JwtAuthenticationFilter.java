package team.jit.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT authentication filter — runs once per request before Spring Security's
 * default filters.
 *
 * What it does:
 *  1. Reads the {@code Authorization} header.
 *  2. Strips the {@code Bearer } prefix to obtain the raw JWT string.
 *  3. Delegates token validation to {@link JwtService}.
 *  4. On success, creates a {@link UsernamePasswordAuthenticationToken} with
 *     the role extracted from the token and stores it in the
 *     {@link SecurityContextHolder} — this makes the request "authenticated"
 *     for the rest of the filter chain.
 *  5. On failure (missing / invalid / expired token) the context is left empty;
 *     Spring Security will then reject the request if the endpoint requires auth.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // No token present → pass through; security rules decide whether to reject
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7); // strip "Bearer "

        if (!jwtService.isTokenValid(jwt)) {
            // Invalid / expired token → clear context and continue; the
            // authorizeHttpRequests rule will reject if the endpoint is protected
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtService.extractUsername(jwt);
        String role     = jwtService.extractRole(jwt);      // e.g. "ROLE_ADMIN"

        // Build an Authentication object with the role as a GrantedAuthority
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        // Store in context so the rest of Spring Security can read it
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}

