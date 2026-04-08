package com.kafka.ordermicroservice.service.dto;

import com.kafka.core.types.OrderStatus;
import com.kafka.ordermicroservice.entity.OrderItem;

import java.util.List;

public record OrderDetailsDto(
        Long orderId,
        Long userId,
        List<OrderItem> items,
        OrderStatus status
) {}
