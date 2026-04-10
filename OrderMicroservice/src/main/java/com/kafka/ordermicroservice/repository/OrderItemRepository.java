package com.kafka.ordermicroservice.repository;


import com.kafka.ordermicroservice.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderId(Long orderId);
    List<OrderItem> findAllByProductId(Long productId);
}
