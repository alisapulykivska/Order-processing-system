package com.kafka.paymentmicroservice.advice;

import com.kafka.core.advice.BaseExceptionHandler;
import com.kafka.core.exception.ErrorResponse;
import com.kafka.core.exception.order.OrderCreationException;
import com.kafka.core.exception.order.OrderNotFoundException;
import com.kafka.core.exception.payment.ShippingServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler extends BaseExceptionHandler {
    @ExceptionHandler(ShippingServiceException.class)
    public ResponseEntity<ErrorResponse> handleDistanceServiceError(ShippingServiceException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse("DISTANCE_SERVICE_ERROR", ex.getMessage()));
    }
}
