package com.kafka.core.event;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReservationCancelledEvent {
    private List<Long> productIds;
    private Long orderId;
}
