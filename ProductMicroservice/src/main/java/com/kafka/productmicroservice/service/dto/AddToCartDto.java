package com.kafka.productmicroservice.service.dto;

import com.kafka.productmicroservice.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartDto {
    private Long userId;
    private Long productId;
    private Integer quantity;
}
