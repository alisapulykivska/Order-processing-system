package com.kafka.ordermicroservice.service;

import com.kafka.core.command.CreateOrderCommand;

public interface OrderService {
    //String createOrderSync(CreateOrderDto createOrderDto) throws ExecutionException, InterruptedException;
    String createOrderAsync(CreateOrderCommand createOrderCommand);
    void approveOrder(Long orderId);
    void rejectOrder(Long orderId);
}
