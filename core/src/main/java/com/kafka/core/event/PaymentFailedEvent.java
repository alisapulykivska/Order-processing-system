package com.kafka.core.event;

import com.kafka.core.entity.ReservedProduct;

import java.util.List;

public record PaymentFailedEvent(Long orderId, List<ReservedProduct> products) {}