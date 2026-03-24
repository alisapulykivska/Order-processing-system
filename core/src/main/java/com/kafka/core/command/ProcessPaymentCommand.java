package com.kafka.core.command;

import com.kafka.core.entity.ReservedProduct;
import java.util.List;


public record ProcessPaymentCommand(Long orderId, List<ReservedProduct> reservedProducts) {}
