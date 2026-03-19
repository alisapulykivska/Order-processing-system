package com.kafka.ordermicroservice.service;

import com.kafka.ordermicroservice.service.dto.CreateOrderDto;

import java.util.concurrent.ExecutionException;

public interface OrderService {
    String createOrderSync(CreateOrderDto createOrderDto) throws ExecutionException, InterruptedException;
    String createOrderAsync(CreateOrderDto createOrderDto);
    void approveOrder(Long orderId);
}
