package com.edu.flysixbackend.services;

import com.edu.flysixbackend.model.User;
import com.edu.flysixbackend.model.UserRole;
import com.edu.flysixbackend.repository.UserRepository;
import com.edu.flysixbackend.repository.UserRoleRepository;
import com.edu.flysixbackend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    void setup() {
        MockitoAnnotations.initMocks(this);
    }

//    @Test
    @DisplayName("Test User Registration")
    public void registerUserTest() throws Exception{

        UserRole role = new UserRole();
        role.setRoleId(1L);
        User user = new User("TestUsername", "asd", "TestName", "0771231231",
                "test@test.com", "testAddress", role);

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        User savedUser = (User) user.clone();
        savedUser.setPassword(encryptedPassword);

        Mockito.when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(savedUser);

        Assertions.assertNotNull(userService.saveUser(user));

    }
}
