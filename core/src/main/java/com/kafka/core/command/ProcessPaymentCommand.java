package com.kafka.core.command;

import com.kafka.core.entity.ReservedProduct;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentCommand {
    private Long orderId;
    List<ReservedProduct> reservedProducts;
}
