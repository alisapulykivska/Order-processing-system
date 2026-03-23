package com.kafka.core.event;

import java.util.HashMap;

public record ProductReservationFailedEvent(Long orderId, HashMap<Long, Integer> products) {}
