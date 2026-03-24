package com.kafka.productmicroservice.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CheckoutDto(
        @NotNull(message = "User ID is required")
        @Positive(message = "User ID must be positive")
        Long userId
) {}