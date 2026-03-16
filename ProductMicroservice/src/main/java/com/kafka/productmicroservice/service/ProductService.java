package com.kafka.productmicroservice.service;

import com.kafka.productmicroservice.service.dto.AddToCartDto;
import com.kafka.productmicroservice.service.dto.CreateProductDto;

public interface ProductService {
    String createProductAsync(CreateProductDto createProductDto);

    String addToCart(AddToCartDto addToCartDto);
}
