package com.kafka.core.event;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReservationCancelledEvent {
    private Long productId;
    private Long orderId;
}
