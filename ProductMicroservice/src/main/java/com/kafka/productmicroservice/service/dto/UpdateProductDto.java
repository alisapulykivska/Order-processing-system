package com.kafka.productmicroservice.service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateProductDto(
        @NotBlank(message = "Title cannot be blank")
        @Size(max = 255, message = "Title cannot exceed 255 characters")
        String title,
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
        BigDecimal price,
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity cannot be negative or zero")
        Integer quantity) {
}
