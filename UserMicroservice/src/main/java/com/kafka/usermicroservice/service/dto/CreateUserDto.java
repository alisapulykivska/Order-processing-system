package com.kafka.usermicroservice.service.dto;

public record CreateUserDto(
        String username,
        String email,
        String password
) {}
