package com.kafka.productmicroservice.repository;

import com.kafka.productmicroservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
