package com.kafka.ordermicroservice.service;

import com.kafka.core.types.OrderStatus;
import com.kafka.ordermicroservice.entity.OrderHistory;
import com.kafka.ordermicroservice.repository.OrderHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OrderHistoryServiceImpl implements OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;

    @Override
    public void add(Long orderId, OrderStatus orderStatus) {
        // exceptions?
        OrderHistory entity = new OrderHistory();
        entity.setOrderId(orderId);
        entity.setStatus(orderStatus);
        entity.setCreatedAt(LocalDateTime.now());
        orderHistoryRepository.save(entity);
    }
}
