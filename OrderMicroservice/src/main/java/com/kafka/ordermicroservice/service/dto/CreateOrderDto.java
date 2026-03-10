package com.kafka.ordermicroservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CreateOrderDto {
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
