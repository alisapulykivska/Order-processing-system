package com.kafka.productmicroservice.advice;

import com.kafka.core.advice.BaseExceptionHandler;
import com.kafka.core.exception.product.CartItemNotFoundException;
import com.kafka.core.exception.product.CartNotFoundException;
import com.kafka.core.exception.ErrorResponse;
import com.kafka.core.exception.product.InsufficientProductQuantityException;
import com.kafka.core.exception.product.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("PRODUCT_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(CartItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("CART_ITEM_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(InsufficientProductQuantityException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientQuantity(InsufficientProductQuantityException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("INSUFFICIENT_QUANTITY", ex.getMessage()));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFound(CartNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("CART_NOT_FOUND", ex.getMessage()));
    }

}
