package com.example.product_restapi;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedAccessToProtectedEndpoint_ShouldFail() throws Exception {
        mockMvc.perform(get("/api/product/get"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser(roles = "USER")
    void authenticatedUserAccessToUserEndpoint_ShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/product/get"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void authenticatedAdminAccessToAdminEndpoint_ShouldSucceed() throws Exception {
        String product = "{ \"name\": \"Mobile\", \"price\": 100 }";
        mockMvc.perform(post("/api/product/add")
                        .contentType("application/json")
                        .content(product))
                .andExpect(status().isOk());
    }
}