package com.edu.flysixbackend.service;

import com.edu.flysixbackend.model.User;

import java.util.Collection;

public interface UserService {

    Collection<User> getAllUsers();
    User saveUser(User user) throws Exception;
    User updateUser(User user) throws Exception;
    Integer deleteUser(User user) throws Exception;
    User getUserByUsername(String username);
    boolean isUsernameAlreadyExists(String username);
}
