package com.kafka.usermicroservice.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateUserDto(
        @NotNull(message = "Username is required")
        String username,
        @NotNull(message = "Email is required")
        @Email(message = "Field must contain valid email")
        String email,
        @NotNull(message = "Owner ID is required")
        @Length(min = 8, message = "Password must be at least 8 characters long")
        String password
) {}
