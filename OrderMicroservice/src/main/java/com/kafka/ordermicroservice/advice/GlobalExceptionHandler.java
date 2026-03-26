package com.kafka.ordermicroservice.advice;

import com.kafka.core.advice.BaseExceptionHandler;
import com.kafka.core.exception.ErrorResponse;
import com.kafka.core.exception.order.OrderCreationException;
import com.kafka.core.exception.order.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler extends BaseExceptionHandler {
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("ORDER_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(OrderCreationException.class)
    public ResponseEntity<ErrorResponse> handleOrderCreationException(OrderCreationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("ORDER_CREATION_FAILED", ex.getMessage()));
    }
}
