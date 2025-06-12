package com.example.product_restapi.repo;

import com.example.product_restapi.entity.UserInfo;
import com.example.product_restapi.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserInfoRepositoryTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    void findByUsername_WhenUserExists_ShouldReturnUser() {
        // Arrange
        UserInfo user = new UserInfo();
        user.setUsername("sam");
        user.setPassword("123");
        user.setEmail("s@gmail.com");
        user.setRoles("ROLE_USER");
        userInfoRepository.save(user);
        // Act
        Optional<UserInfo> foundUser = userInfoRepository.findByUsername("sam");
        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("sam", foundUser.get().getUsername());
    }
    @Test
    void findByUsername_WhenUserNotExists_ShouldReturnEmpty() {
        // Act
        Optional<UserInfo> foundUser = userInfoRepository.findByUsername("nonexistent");
        // Assert
        assertFalse(foundUser.isPresent());
    }
}