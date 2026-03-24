package com.kafka.core.event;

import java.util.List;

public record ProductReservationCancelledEvent(List<Long> productIds, Long orderId) {}