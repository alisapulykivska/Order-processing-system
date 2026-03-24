package com.kafka.productmicroservice.service.dto;

public record AddToCartDto(Long userId, Long productId, Integer quantity) {}