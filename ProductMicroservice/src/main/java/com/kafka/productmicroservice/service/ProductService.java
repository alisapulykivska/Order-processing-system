package com.kafka.productmicroservice.service;

import com.kafka.productmicroservice.entity.Product;
import com.kafka.productmicroservice.service.dto.AddToCartDto;
import com.kafka.productmicroservice.service.dto.CreateProductDto;

public interface ProductService {
    String createProductAsync(CreateProductDto createProductDto);
    Product reserve(Product desiredProduct, Long orderId);
    String addToCart(AddToCartDto addToCartDto);
    void cancelReservation(Product productToCancel, Long orderId);
}
