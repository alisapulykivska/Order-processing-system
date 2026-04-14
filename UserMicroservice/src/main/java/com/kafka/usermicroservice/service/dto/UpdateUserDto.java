package com.kafka.usermicroservice.service.dto;

public record UpdateUserDto(
        String email,
        String password
) {}
