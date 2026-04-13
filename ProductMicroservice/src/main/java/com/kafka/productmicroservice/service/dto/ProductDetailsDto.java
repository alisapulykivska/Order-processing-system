package com.kafka.productmicroservice.service.dto;

import com.kafka.core.dto.GetAmountOfOrderedByProductDto;
import com.kafka.productmicroservice.entity.Product;

import java.util.List;

public record ProductDetailsDto(Product product, List<GetAmountOfOrderedByProductDto> amounts) {
}
