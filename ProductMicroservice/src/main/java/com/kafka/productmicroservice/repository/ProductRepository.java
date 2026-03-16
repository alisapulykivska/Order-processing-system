package com.kafka.productmicroservice.repository;

import com.kafka.productmicroservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
