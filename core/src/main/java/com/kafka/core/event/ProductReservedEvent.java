package com.kafka.core.event;

import com.kafka.core.entity.ReservedProduct;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReservedEvent {
    private Long orderId;
    private List<ReservedProduct> reservedProducts;
}
