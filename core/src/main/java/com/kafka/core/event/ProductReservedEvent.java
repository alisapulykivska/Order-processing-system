package com.kafka.core.event;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReservedEvent {
    private Long orderId;
    private Long productId;
    private BigDecimal productPrice;
    private Integer productQuantity;
}
