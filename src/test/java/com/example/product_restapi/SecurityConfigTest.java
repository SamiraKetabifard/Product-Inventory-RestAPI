package com.example.product_restapi;

import com.example.product_restapi.service.UserInfoDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testUserDetailsServiceBean() {
        assertNotNull(userDetailsService, "UserDetailsService should be configured");
        assertTrue(userDetailsService instanceof UserInfoDetailService,
                "UserDetailsService should be instance of UserInfoDetailService");
    }
    @Test
    void testPasswordEncoderBean() {
        assertNotNull(passwordEncoder, "PasswordEncoder should be configured");
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword),
                "PasswordEncoder should correctly match raw and encoded passwords");
    }

    @Test
    void testFailedAuthentication() throws Exception {
        mockMvc.perform(formLogin().user("nonexistent").password("wrong"))
                .andExpect(unauthenticated());
    }
}
