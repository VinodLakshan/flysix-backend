package com.edu.flysixbackend.controller;

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
            User registerUser = userService.registerUser(user);

            if(registerUser != null) {
                log.info("User is created");
                return ResponseEntity.ok(jwtUtil.authenticate(user));

            }else{
                log.error("user registration is failed");
                throw new Exception("User registration is failed");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
