package team.jit.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import team.jit.config.security.JwtService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("Fetch employees")
    public void fetchEmployees() throws Exception {
        // Use a CUSTOMER token — read-only access is sufficient for a GET request
        String token = jwtService.generateToken("test-customer", "ROLE_CUSTOMER");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/employees")
                .header("Authorization", "Bearer " + token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(12));
    }
}
