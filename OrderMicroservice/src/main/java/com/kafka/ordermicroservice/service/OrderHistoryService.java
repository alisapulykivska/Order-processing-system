package com.kafka.ordermicroservice.service;

import com.kafka.core.types.OrderStatus;
import com.kafka.ordermicroservice.service.dto.CreateOrderDto;

import java.util.concurrent.ExecutionException;

public interface OrderHistoryService {
    void add(Long orderId, OrderStatus orderStatus);
}
