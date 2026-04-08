package com.kafka.ordermicroservice.service;

import com.kafka.core.command.CreateOrderCommand;
import com.kafka.ordermicroservice.entity.Order;
import com.kafka.ordermicroservice.service.dto.OrderDetailsDto;

import java.util.List;

public interface OrderService {
    //String createOrderSync(CreateOrderDto createOrderDto) throws ExecutionException, InterruptedException;
    String createOrderAsync(CreateOrderCommand createOrderCommand);
    void approveOrder(Long orderId);
    void rejectOrder(Long orderId);
    List<Order> getAllOrders();
    OrderDetailsDto getOrderById(Long orderId);
}
