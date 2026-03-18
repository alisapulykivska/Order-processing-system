package com.kafka.core.event;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
        private Long orderId;
        private Long customerId;
        private Long productId;
        private Integer productQuantity;

}