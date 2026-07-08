package team.jit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import team.jit.config.security.JwtService;
import team.jit.dto.EmployeeForm;
import team.jit.dto.EmployeeUpdateForm;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link EmployeeController}.
 *
 * Covers all CRUD endpoints and verifies role-based access control:
 *  - CUSTOMER  → allowed on GET, forbidden on POST / PUT / DELETE
 *  - OPERATOR  → allowed on GET / POST / PUT, forbidden on DELETE
 *  - ADMIN     → allowed on everything including DELETE
 *
 * Tests run in a fixed order so the employee created in the POST test
 * is available for subsequent PUT / DELETE tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeControllerIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired JwtService jwtService;

    // Tokens — generated once per class, reused across tests
    private String customerToken() { return bearer("alice",   "ROLE_CUSTOMER"); }
    private String operatorToken() { return bearer("bob",     "ROLE_OPERATOR"); }
    private String adminToken()    { return bearer("charlie", "ROLE_ADMIN");    }

    private String bearer(String user, String role) {
        return "Bearer " + jwtService.generateToken(user, role);
    }

    /** ID of the employee created during the POST test — shared between tests. */
    private static Long createdId;

    // =========================================================================
    // GET /employees  — list all
    // =========================================================================

    @Test
    @Order(1)
    @DisplayName("GET /employees — CUSTOMER can list all employees (12 seed rows)")
    void getAllEmployees_asCustomer_returns200() throws Exception {
        mvc.perform(get("/employees")
                .header("Authorization", customerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(12)));
    }

    @Test
    @Order(2)
    @DisplayName("GET /employees — 401 without token")
    void getAllEmployees_noToken_returns401() throws Exception {
        mvc.perform(get("/employees"))
                .andExpect(status().isUnauthorized());
    }

    // =========================================================================
    // GET /employees/{id}  — find by id
    // =========================================================================

    @Test
    @Order(3)
    @DisplayName("GET /employees/1 — CUSTOMER can read a single employee")
    void getEmployeeById_asCustomer_returns200() throws Exception {
        mvc.perform(get("/employees/1")
                .header("Authorization", customerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.surname").value("Smith"));
    }

    @Test
    @Order(4)
    @DisplayName("GET /employees/9999 — returns 404 for unknown id")
    void getEmployeeById_notFound_returns404() throws Exception {
        mvc.perform(get("/employees/9999")
                .header("Authorization", customerToken()))
                .andExpect(status().isNotFound());
    }

    // =========================================================================
    // GET /employees/paged  — paginated list
    // =========================================================================

    @Test
    @Order(5)
    @DisplayName("GET /employees/paged — OPERATOR receives first page with correct total")
    void getPagedEmployees_asOperator_returns200() throws Exception {
        mvc.perform(get("/employees/paged")
                .header("Authorization", operatorToken())
                .param("size", "5")
                .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(12))
                .andExpect(jsonPath("$.content", hasSize(5)));
    }

    @Test
    @Order(6)
    @DisplayName("GET /employees/paged — filter by name returns matching employees")
    void getPagedEmployees_filteredByName_returnsMatch() throws Exception {
        mvc.perform(get("/employees/paged")
                .header("Authorization", customerToken())
                .param("name", "Bob"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Bob"));
    }

    // =========================================================================
    // POST /employees  — create
    // =========================================================================

    @Test
    @Order(7)
    @DisplayName("POST /employees — OPERATOR creates a new employee → 201")
    void createEmployee_asOperator_returns201() throws Exception {
        EmployeeForm form = new EmployeeForm();
        form.setName("Test");
        form.setSurname("User");
        form.setPersonalId("IT-TEST-001");
        form.setSalary(BigDecimal.valueOf(5000));

        String body = mvc.perform(post("/employees")
                .header("Authorization", operatorToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.surname").value("User"))
                .andExpect(jsonPath("$.personalId").value("IT-TEST-001"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Capture the generated id for use in later tests
        createdId = objectMapper.readTree(body).get("id").asLong();
    }

    @Test
    @Order(8)
    @DisplayName("POST /employees — CUSTOMER is forbidden → 403")
    void createEmployee_asCustomer_returns403() throws Exception {
        EmployeeForm form = new EmployeeForm();
        form.setName("Should");
        form.setSurname("Fail");
        form.setPersonalId("IT-FORBIDDEN-001");
        form.setSalary(BigDecimal.valueOf(1000));

        mvc.perform(post("/employees")
                .header("Authorization", customerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(9)
    @DisplayName("POST /employees — invalid body (missing required fields) → 400")
    void createEmployee_invalidBody_returns400() throws Exception {
        // Empty form — @NotBlank on name / surname / personalId will fire
        EmployeeForm empty = new EmployeeForm();

        mvc.perform(post("/employees")
                .header("Authorization", operatorToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empty)))
                .andExpect(status().isBadRequest());
    }

    // =========================================================================
    // PUT /employees/{id}  — update
    // =========================================================================

    @Test
    @Order(10)
    @DisplayName("PUT /employees/{id} — OPERATOR updates the created employee")
    void updateEmployee_asOperator_returns200() throws Exception {
        EmployeeUpdateForm form = new EmployeeUpdateForm();
        form.setName("Updated");
        form.setSalary(BigDecimal.valueOf(7500));

        mvc.perform(put("/employees/" + createdId)
                .header("Authorization", operatorToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.salary").value(7500));
    }

    @Test
    @Order(11)
    @DisplayName("PUT /employees/{id} — CUSTOMER is forbidden → 403")
    void updateEmployee_asCustomer_returns403() throws Exception {
        EmployeeUpdateForm form = new EmployeeUpdateForm();
        form.setName("HackerAttempt");

        mvc.perform(put("/employees/1")
                .header("Authorization", customerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(12)
    @DisplayName("PUT /employees/{id} — ADMIN updates seed employee")
    void updateEmployee_asAdmin_returns200() throws Exception {
        EmployeeUpdateForm form = new EmployeeUpdateForm();
        form.setName("AdminUpdated");
        form.setSalary(BigDecimal.valueOf(12000));

        mvc.perform(put("/employees/1")
                .header("Authorization", adminToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("AdminUpdated"));
    }

    // =========================================================================
    // DELETE /employees/{id}
    // =========================================================================

    @Test
    @Order(13)
    @DisplayName("DELETE /employees/{id} — OPERATOR is forbidden → 403")
    void deleteEmployee_asOperator_returns403() throws Exception {
        mvc.perform(delete("/employees/" + createdId)
                .header("Authorization", operatorToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(14)
    @DisplayName("DELETE /employees/{id} — ADMIN deletes the created employee → 200")
    void deleteEmployee_asAdmin_returns200() throws Exception {
        mvc.perform(delete("/employees/" + createdId)
                .header("Authorization", adminToken()))
                .andExpect(status().isOk());
    }

    @Test
    @Order(15)
    @DisplayName("DELETE /employees/{id} — 404 after the employee was already deleted")
    void deleteEmployee_alreadyDeleted_returns404() throws Exception {
        mvc.perform(delete("/employees/" + createdId)
                .header("Authorization", adminToken()))
                .andExpect(status().isNotFound());
    }
}

