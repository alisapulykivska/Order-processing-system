package com.kafka.ordermicroservice.service.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
}
