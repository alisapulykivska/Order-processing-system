package com.kafka.core.command;

import com.kafka.core.dto.OrderItemDto;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderCommand {
    private Long userId;
    private List<OrderItemDto> orderItems;
}
