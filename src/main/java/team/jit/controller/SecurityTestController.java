package team.jit.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Demo controller that showcases JWT role-based access control.
 *
 * All other controllers in this application are fully public (no token needed).
 * This is the ONLY controller that enforces role checks via @PreAuthorize.
 *
 * Roles (issued by AuthController / GET /auth/mock-tokens):
 *  - ROLE_CUSTOMER  → can reach /security-test/customer only
 *  - ROLE_OPERATOR  → can reach /security-test/customer and /security-test/operator
 *  - ROLE_ADMIN     → can reach all endpoints including /security-test/admin
 */
@RestController
@RequestMapping("/security-test")
public class SecurityTestController {

    /**
     * Publicly accessible — no token required.
     * Useful for verifying that the endpoint is reachable before adding a token.
     */
    @GetMapping("/public")
    public Map<String, String> publicEndpoint() {
        return Map.of(
                "endpoint", "public",
                "message", "Anyone can access this endpoint — no token required."
        );
    }

    /**
     * Requires ROLE_CUSTOMER, ROLE_OPERATOR, or ROLE_ADMIN.
     * A valid JWT with any of these roles grants access.
     */
    @GetMapping("/customer")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OPERATOR', 'ADMIN')")
    public Map<String, String> customerEndpoint() {
        return Map.of(
                "endpoint", "customer",
                "message", "Access granted — you have at least ROLE_CUSTOMER."
        );
    }

    /**
     * Requires ROLE_OPERATOR or ROLE_ADMIN.
     * A ROLE_CUSTOMER token will receive 403 Forbidden here.
     */
    @GetMapping("/operator")
    @PreAuthorize("hasAnyRole('OPERATOR', 'ADMIN')")
    public Map<String, String> operatorEndpoint() {
        return Map.of(
                "endpoint", "operator",
                "message", "Access granted — you have at least ROLE_OPERATOR."
        );
    }

    /**
     * Requires ROLE_ADMIN only.
     * Only tokens issued for the ADMIN role may reach this endpoint.
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> adminEndpoint() {
        return Map.of(
                "endpoint", "admin",
                "message", "Access granted — you have ROLE_ADMIN."
        );
    }
}

