package com.kafka.core.command;

import java.util.HashMap;

public record ReserveProductCommand(HashMap<Long, Integer> products, Long orderId) {}
