package com.kafka.core.event;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReservationFailedEvent {
    private Long productId;
    private Long orderId;
    private Integer productQuantity;
}
