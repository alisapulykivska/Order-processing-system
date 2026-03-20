package com.kafka.core.event;

import com.kafka.core.entity.ReservedProduct;
import lombok.*;

import java.util.HashMap;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReservationFailedEvent {
    private Long orderId;
    private HashMap<Long, Integer> products;
}
