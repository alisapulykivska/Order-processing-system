package com.kafka.core.exception;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(Long userId) {
        super("Cart not found for user " + userId);
    }
}

