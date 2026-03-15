package com.kafka.ordermicroservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateOrderDto {
    private Long userId;
    private List<OrderItemDto> orderItems;
}
