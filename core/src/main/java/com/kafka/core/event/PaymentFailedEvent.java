package com.kafka.core.event;

import com.kafka.core.entity.ReservedProduct;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFailedEvent {
    private Long orderId;
    private List<ReservedProduct> products;
}
