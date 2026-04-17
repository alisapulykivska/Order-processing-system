package com.kafka.usermicroservice.service.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record UpdateUserDto(
        @NotNull(message = "Email is required")
        @Email(message = "Field must contain valid email")
        String email,
        @NotNull(message = "Email is required")
        @Length(min = 8, message = "Password must be at least 8 characters long")
        String password
) {}
