package com.kafka.productmicroservice.service.dto;

import com.kafka.productmicroservice.entity.Product;

import java.util.Map;

public record ProductOrdersDto (Product product, Map<Long, Integer> amountInOrder) {
}
