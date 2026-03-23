package com.kafka.core.event;

import java.util.HashMap;

public record OrderCreatedEvent(Long orderId, Long customerId, HashMap<Long, Integer> products) {}