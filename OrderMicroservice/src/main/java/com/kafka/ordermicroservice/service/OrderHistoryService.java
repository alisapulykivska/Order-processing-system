package com.kafka.ordermicroservice.service;

import com.kafka.core.types.OrderStatus;

public interface OrderHistoryService {
    void add(Long orderId, OrderStatus orderStatus);
}
