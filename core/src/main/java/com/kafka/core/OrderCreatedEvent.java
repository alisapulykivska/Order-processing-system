package com.kafka.core;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

        private String orderId;
        private String title;
        private BigDecimal price;
        private Integer quantity;

}