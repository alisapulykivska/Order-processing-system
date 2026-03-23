package com.kafka.core.command;

import com.kafka.core.dto.OrderItemDto;
import java.util.List;

public record CreateOrderCommand(Long userId, List<OrderItemDto> orderItems) {}
