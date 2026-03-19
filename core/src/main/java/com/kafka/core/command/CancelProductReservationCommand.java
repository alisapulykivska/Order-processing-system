package com.kafka.core.command;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelProductReservationCommand {
    private Long productId;
    private Long orderId;
    private Integer productQuantity;
}
