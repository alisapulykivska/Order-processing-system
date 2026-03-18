package com.kafka.core.command;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveProductCommand {
    private String title;
    private Integer quantity;
    private BigDecimal price;
}
