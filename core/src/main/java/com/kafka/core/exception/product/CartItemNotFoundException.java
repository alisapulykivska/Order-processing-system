package com.kafka.core.exception.product;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(Long productId, Long userId) {
        super("Product with id " + productId + " in cart of User with id" + userId + " not found");
    }
}

