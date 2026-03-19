package com.kafka.core.event;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
        private Long orderId;
        private Long customerId;
        private HashMap<Long, Integer> products;
        //private Long productId;
        //private Integer productQuantity;

}