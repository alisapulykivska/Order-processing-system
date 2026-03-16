package com.kafka.core.event;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreatedEvent {

    private String orderId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

}
