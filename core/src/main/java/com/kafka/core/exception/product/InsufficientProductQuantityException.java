package com.kafka.core.exception.product;

public class InsufficientProductQuantityException extends RuntimeException {

    public InsufficientProductQuantityException(Long productId, Long orderId) {
        super("Insufficient quantity for product " + productId + " in order " + orderId);
    }
}

