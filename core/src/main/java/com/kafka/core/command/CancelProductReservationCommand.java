package com.kafka.core.command;

import com.kafka.core.entity.ReservedProduct;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelProductReservationCommand {
    private Long orderId;
    private List<ReservedProduct> products;
}
