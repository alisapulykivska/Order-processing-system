package com.kafka.core.command;

import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveProductCommand {
    private HashMap<Long, Integer> products;
    private Long orderId;
}
