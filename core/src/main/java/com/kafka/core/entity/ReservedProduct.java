package com.kafka.core.entity;

import java.math.BigDecimal;

public record ReservedProduct(Long productId, BigDecimal price, Integer quantity) {}