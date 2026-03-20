package com.kafka.paymentmicroservice.repository;

import com.kafka.paymentmicroservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
