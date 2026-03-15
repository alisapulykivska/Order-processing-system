package com.kafka.ordermicroservice.repository;


import com.kafka.ordermicroservice.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
