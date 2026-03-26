package com.kafka.core.exception.order;

public class OrderCreationException extends RuntimeException {

    public OrderCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
