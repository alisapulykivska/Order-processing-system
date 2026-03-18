package com.kafka.core.command;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentCommand {
    private Long orderId;
    private Long productId;
    private BigDecimal productPrice;
    private Integer productQuantity;
}
