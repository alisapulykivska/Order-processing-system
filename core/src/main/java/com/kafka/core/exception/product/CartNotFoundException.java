package com.kafka.core.exception.product;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(Long userId) {
        super("Cart not found for user " + userId);
    }
}

