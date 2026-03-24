package com.kafka.core.event;

public record PaymentProcessedEvent(Long orderId, Long paymentId) {}
