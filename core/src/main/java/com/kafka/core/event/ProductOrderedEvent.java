package com.kafka.core.event;

import java.math.BigDecimal;

public record ProductOrderedEvent(String orderId, String title, BigDecimal price, Integer quantity) {}
