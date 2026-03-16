package com.kafka.productmicroservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CreateProductDto {
    private Long ownerId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
