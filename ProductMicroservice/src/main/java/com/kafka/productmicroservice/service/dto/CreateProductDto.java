package com.kafka.productmicroservice.service.dto;
import java.math.BigDecimal;

public record CreateProductDto(Long ownerId, String title, BigDecimal price, Integer quantity) { }