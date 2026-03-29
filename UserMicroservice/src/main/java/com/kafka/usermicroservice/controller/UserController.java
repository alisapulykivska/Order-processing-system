package com.kafka.usermicroservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

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
}
