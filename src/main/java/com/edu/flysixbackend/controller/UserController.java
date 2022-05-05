package com.edu.flysixbackend.controller;

import com.edu.flysixbackend.dto.ErrorDto;
import com.edu.flysixbackend.model.User;
import com.edu.flysixbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestBody User user) {

        log.info("User is being deleted");
        try {
            return ResponseEntity.ok(userService.deleteUser(user));

        } catch (Exception e) {
            log.info("User deletion failed");
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {

        log.info("User is being updated");

        try {
            User updatedUser = userService.updateUser(user);
            log.info("User is being updated");
            return ResponseEntity.ok(updatedUser);

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
