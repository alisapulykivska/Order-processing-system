package com.kafka.core.dto;

import java.math.BigDecimal;

public record OrderItemDto(Long productId, String productName, BigDecimal productPrice, Integer quantity) {}