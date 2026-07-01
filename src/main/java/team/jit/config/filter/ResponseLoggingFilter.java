package team.jit.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Servlet filter that logs every HTTP response after the request has been fully processed.
 *
 * Key concepts:
 *  - OncePerRequestFilter : Spring helper — guarantees the filter runs exactly once per request,
 *                           even in async dispatch or forward scenarios.
 *  - doFilterInternal()   : wrap the chain call so we can inspect the response *after* the handler runs.
 *  - @Component           : registers the filter in the Spring context; Boot auto-registers it in the chain.
 *
 * Logging format:
 *   [RESPONSE] user=<user> | method=GET | uri=/employees/paged | status=200 | duration=12ms
 */
@Slf4j
@Component
@Order(value = 2)
public class ResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        // Let the request proceed through the rest of the filter chain and the handler
        filterChain.doFilter(request, response);

        // --- Everything below runs AFTER the response has been written ---

        long duration = System.currentTimeMillis() - startTime;

        // In a real app: SecurityContextHolder.getContext().getAuthentication().getName()
        String user = getMockedUser(request);

        log.info("[RESPONSE] user={} | method={} | uri={} | status={} | duration={}ms",
                user,
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration);
    }

    /**
     * Mock user resolution.
     *
     * In production you would read this from Spring Security context:
     *   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
     *   return auth != null ? auth.getName() : "anonymous";
     *
     * Or from a JWT claim / request header:
     *   return request.getHeader("X-User-Id");
     */
    private String getMockedUser(HttpServletRequest request) {
        // Check if a simple header is present (easy to test with curl/Postman)
        String userHeader = request.getHeader("X-User-Id");
        if (userHeader != null && !userHeader.isBlank()) {
            return userHeader;
        }
        // Default mocked user for workshop purposes
        return "mock-user@workshop.local";
    }
}

