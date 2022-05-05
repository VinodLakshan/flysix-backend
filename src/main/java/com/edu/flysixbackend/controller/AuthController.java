package com.edu.flysixbackend.controller;

import com.edu.flysixbackend.dto.ErrorDto;
import com.edu.flysixbackend.exception.UsernameAlreadyExistException;
import com.edu.flysixbackend.model.User;
import com.edu.flysixbackend.service.UserService;
import com.edu.flysixbackend.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Slf4j
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody User user){
        log.info("User is logging in");
        return ResponseEntity.ok(jwtUtil.authenticate(user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){

        log.info("User is being registered");

        try {
            userService.saveUser(user);
            log.info("User is created");
            return ResponseEntity.ok(jwtUtil.authenticate(user));

        } catch (UsernameAlreadyExistException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
