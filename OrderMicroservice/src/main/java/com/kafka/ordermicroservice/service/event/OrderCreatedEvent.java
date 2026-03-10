package com.kafka.ordermicroservice.service.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor // needed for serialization to work
@AllArgsConstructor
public class OrderCreatedEvent {
        private String orderId;
        private String title;
        private BigDecimal price;
        private Integer quantity;

}
