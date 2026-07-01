package team.jit.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Simple security config — all endpoints are open (permit-all).
 *
 * Key concepts:
 *  - SecurityFilterChain  : the modern way to configure Spring Security (replaces WebSecurityConfigurerAdapter)
 *  - cors()               : delegates CORS pre-flight handling to Spring Security before any auth check
 *  - csrf disabled        : standard for stateless REST APIs consumed by a JS frontend
 *  - SessionCreationPolicy.STATELESS : no HTTP session — every request must carry its own credentials (JWT etc.)
 *  - CorsConfigurationSource : single place that defines allowed origins/methods/headers for the whole app
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Hand CORS pre-flight (OPTIONS) requests to our CorsConfigurationSource bean
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Disable CSRF — not needed for stateless REST APIs
            .csrf(AbstractHttpConfigurer::disable)

            // No HTTP session — REST APIs are stateless
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Allow every request without authentication (workshop / learning purpose)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
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

