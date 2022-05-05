package com.edu.flysixbackend.services;

import com.edu.flysixbackend.exception.UsernameAlreadyExistException;
import com.edu.flysixbackend.model.User;
import com.edu.flysixbackend.model.UserRole;
import com.edu.flysixbackend.repository.UserRepository;
import com.edu.flysixbackend.repository.UserRoleRepository;
import com.edu.flysixbackend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UserRoleRepository roleRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @InjectMocks
    private UserServiceImpl userServiceMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private User getUser() {
        UserRole role = new UserRole();
        role.setRoleId(1L);
        return new User("TestUsername", "asd", "TestName", "0771231231",
                "test@test.com", "testAddress", role);

    }

    @Test
    @DisplayName("Test User Registration")
    public void registerUserTest() throws Exception{

        User user = this.getUser();

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        User savedUser = (User) user.clone();
        savedUser.setPassword(encryptedPassword);

        Mockito.when(userRepositoryMock.existsByUsername(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(passwordEncoderMock.encode(ArgumentMatchers.anyString())).thenReturn(encryptedPassword);
        Mockito.when(roleRepositoryMock.getById(ArgumentMatchers.anyLong())).thenReturn(user.getRole());
        Mockito.when(userRepositoryMock.saveAndFlush(ArgumentMatchers.any(User.class))).thenReturn(savedUser);

        Assertions.assertNotNull(userServiceMock.saveUser(user));

    }

    @Test
    @DisplayName("Test User registration when username already exists")
    public void RegisterUserWhenUsernameExists() {

        User user = this.getUser();

        Mockito.when(userRepositoryMock.existsByUsername(ArgumentMatchers.anyString())).thenReturn(true);
        Assertions.assertThrows(UsernameAlreadyExistException.class, () -> {
            userServiceMock.saveUser(user);
        });
    }
}
