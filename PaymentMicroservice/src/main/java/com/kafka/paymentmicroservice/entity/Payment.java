package com.kafka.paymentmicroservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    //private Long productId;
    //private BigDecimal productPrice;
    //private Integer productQuantity;
    private BigDecimal totalPrice;

    public Payment(Long orderId, BigDecimal totalPrice) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
    }
}
