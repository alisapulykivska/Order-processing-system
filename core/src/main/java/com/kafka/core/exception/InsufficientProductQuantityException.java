package com.kafka.core.exception;

public class InsufficientProductQuantityException extends RuntimeException {

    public InsufficientProductQuantityException(Long productId, Long orderId) {
        super("Insufficient quantity for product " + productId + " in order " + orderId);
    }
}

