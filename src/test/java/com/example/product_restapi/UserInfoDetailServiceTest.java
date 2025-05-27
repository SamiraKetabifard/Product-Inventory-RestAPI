package com.example.product_restapi;

import com.example.product_restapi.entity.UserInfo;
import com.example.product_restapi.repository.UserInfoRepository;
import com.example.product_restapi.service.UserInfoDetailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInfoDetailServiceTest {

    @Mock
    private UserInfoRepository userInfoRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserInfoDetailService userInfoDetailService;

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        //Arrange
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("samira");
        userInfo.setPassword("12");
        userInfo.setRoles("ROLE_USER");
        //Act
        when(userInfoRepository.findByUsername("samira")).thenReturn(Optional.of(userInfo));
        UserDetails userDetails = userInfoDetailService.loadUserByUsername("samira");
        //Assert
        assertNotNull(userDetails);
        assertEquals("samira", userDetails.getUsername());
    }
    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        //Arrange
        when(userInfoRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(UsernameNotFoundException.class, () ->
            userInfoDetailService.loadUserByUsername("nonexistent")
        );
    }
    @Test
    void addUser_ShouldEncodePasswordAndSaveUser() {
        //Arrange
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("zahra");
        userInfo.setPassword("12");
        when(passwordEncoder.encode("12")).thenReturn("encodedpassword");
        when(userInfoRepository.save(any(UserInfo.class))).thenReturn(userInfo);
        //Act
        String result = userInfoDetailService.addUser(userInfo);
        //Assert
        assertEquals("User added to system successfully.", result.trim());
        verify(passwordEncoder, times(1)).encode("12");
        verify(userInfoRepository, times(1)).save(any(UserInfo.class));
    }
}