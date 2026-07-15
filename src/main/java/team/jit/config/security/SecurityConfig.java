package team.jit.config.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * JWT-based security config.
 *
 * Access rules:
 *  - /security-test/** → role-checked via @PreAuthorize on SecurityTestController
 *  - everything else   → fully public (no token required)
 *
 * The JWT filter still runs on every request so that when a token IS present
 * the SecurityContext is populated and @PreAuthorize can evaluate roles.
 *
 * Key concepts:
 *  - SecurityFilterChain    : modern way to configure Spring Security
 *  - JwtAuthenticationFilter: validates the Bearer JWT on every request and populates SecurityContext
 *  - cors()                 : delegates CORS pre-flight to CorsConfigurationSource bean
 *  - csrf disabled          : standard for stateless REST APIs
 *  - SessionCreationPolicy.STATELESS : no HTTP session — every request must carry its own JWT
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Hand CORS pre-flight (OPTIONS) requests to our CorsConfigurationSource bean
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Disable CSRF — not needed for stateless REST APIs
            .csrf(AbstractHttpConfigurer::disable)

            // No HTTP session — every request must carry its own JWT
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Role-based access rules
            .authorizeHttpRequests(auth -> auth

                // Public endpoints — no token required
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/mvc/**").permitAll()
                .requestMatchers("/error").permitAll()
                // MessageSource demo — fully public so students can test without a token
                .requestMatchers("/messages/**").permitAll()
                //.requestMatchers(HttpMethod.POST,  "/api/employees/**").hasAnyRole("OPERATOR", "ADMIN")

                // Fine-grained role checks are handled by @PreAuthorize on each controller method.
                // Every other request still requires a valid JWT.
                //.anyRequest().authenticated()
                .anyRequest().permitAll()
            )

            // Register the JWT filter BEFORE the default username/password filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

            // Return 401 for requests that carry no / invalid token (unauthenticated).
            // Without this, Spring Security defaults to 403 for anonymous access.
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(unauthorizedEntryPoint()))

            // Allow H2 console to render frames
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    /**
     * Returns HTTP 401 Unauthorized when a request reaches a protected endpoint
     * without any (or with an invalid) JWT.
     *
     * Why needed: Spring Security's default AccessDeniedHandler returns 403 even for
     * completely anonymous (unauthenticated) requests.  The correct semantic is:
     *   401 — "who are you?" (no / bad credentials)
     *   403 — "I know who you are, but you're not allowed" (authenticated but wrong role)
     */
    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    /**
     * Global CORS policy — applied to every endpoint.
     *
     * For a real app you would restrict:
     *   - allowedOrigins  → your frontend domain, e.g. "https://myapp.com"
     *   - allowedMethods  → only the HTTP verbs you actually use
     *   - allowedHeaders  → "Authorization", "Content-Type", etc.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of("*"));          // any origin (dev convenience)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));                 // accept all request headers
        config.setExposedHeaders(List.of("Location", "X-Total-Count")); // headers JS can read
        config.setAllowCredentials(true);                       // allow cookies / Authorization header
        config.setMaxAge(3600L);                                // cache pre-flight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);       // apply to every path
        return source;
    }
}

