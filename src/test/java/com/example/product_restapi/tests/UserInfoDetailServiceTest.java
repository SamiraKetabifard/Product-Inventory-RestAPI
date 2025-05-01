package com.example.product_restapi.tests;

import com.example.product_restapi.entity.UserInfo;
import com.example.product_restapi.repository.UserInfoRepository;
import com.example.product_restapi.service.UserInfoDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserInfoDetailServiceTest {

    @Mock
    private UserInfoRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserInfoDetailService userInfoDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("testuser");
        userInfo.setPassword("password");
        userInfo.setRoles("ROLE_USER");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userInfo));

        UserDetails userDetails = userInfoDetailService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userInfoDetailService.loadUserByUsername("nonexistent");
        });
    }

    @Test
    void addUser_ShouldEncodePasswordAndSaveUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("newuser");
        userInfo.setPassword("rawpassword");

        when(passwordEncoder.encode("rawpassword")).thenReturn("encodedpassword");
        when(userRepository.save(any(UserInfo.class))).thenReturn(userInfo);

        String result = userInfoDetailService.addUser(userInfo);

        assertEquals("User added to system sucessfully.", result.trim());
        verify(passwordEncoder, times(1)).encode("rawpassword");
        verify(userRepository, times(1)).save(any(UserInfo.class));
    }
}