package com.kafka.paymentmicroservice.service;

import com.kafka.core.entity.ReservedProduct;
import com.kafka.paymentmicroservice.entity.Payment;

import java.util.List;

public interface PaymentService {
    //List<Payment> findAll();
    Payment process(List<ReservedProduct> products, Long orderId);
}
