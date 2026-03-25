package com.kafka.productmicroservice.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddToCartDto(
        @NotNull(message = "User ID is required")
        @Positive(message = "User ID must be positive")
        Long userId,
        @NotNull(message = "Product ID is required")
        @Positive(message = "Product ID must be positive")
        Long productId,
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than 0")
        Integer quantity
) {}