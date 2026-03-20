package com.kafka.core.entity;

import lombok.*;

import java.math.BigDecimal;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservedProduct {
    private Long productId;
    private BigDecimal price;
    private Integer quantity;
}
