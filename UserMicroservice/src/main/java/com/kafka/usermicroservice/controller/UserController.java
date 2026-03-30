package com.kafka.usermicroservice.controller;

import com.kafka.usermicroservice.service.UserService;
import com.kafka.usermicroservice.service.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    /*
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String test(){
        return "hello user!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin')")
    public String test_admin(){
        return "hello Admin!!!";
    }
     */

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto user) {
        userService.registerUser(user.username(), user.email(), user.password());
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

}
