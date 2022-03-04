package com.edu.flysixbackend.service.impl;

import com.edu.flysixbackend.dto.AuthUserDto;
import com.edu.flysixbackend.exception.UsernameAlreadyExistException;
import com.edu.flysixbackend.model.User;
import com.edu.flysixbackend.repository.UserRepository;
import com.edu.flysixbackend.repository.UserRoleRepository;
import com.edu.flysixbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) throws Exception {

        if (!this.isUsernameAlreadyExists(user.getUsername())) {

            User clonedUser = (User) user.clone();
            clonedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.saveAndFlush(clonedUser);
            if (savedUser != null) savedUser.setRole(roleRepository.getById(savedUser.getRole().getRoleId()));
            return savedUser;

        } else {
            throw new UsernameAlreadyExistException("Username is already taken!");
        }
    }

    @Override
    public User updateUser(User user) throws Exception {
        User clonedUser = (User) user.clone();
        clonedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.saveAndFlush(clonedUser);
        if (savedUser != null) savedUser.setRole(roleRepository.getById(savedUser.getRole().getRoleId()));
        return savedUser;
    }

    @Override
    public Integer deleteUser(User user) throws Exception{
        try {
            userRepository.delete(user);
            log.info("User is Deleted");
            return 1;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean isUsernameAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByUsername = this.getUserByUsername(username);

        if (userByUsername == null) {
            log.error("User not found.");
            throw new UsernameNotFoundException("User not found");
        }

        log.info("User found by username");
        return new AuthUserDto(userByUsername);

    }
}
