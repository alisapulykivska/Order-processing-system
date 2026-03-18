package com.kafka.ordermicroservice.repository;

import com.kafka.ordermicroservice.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
}
