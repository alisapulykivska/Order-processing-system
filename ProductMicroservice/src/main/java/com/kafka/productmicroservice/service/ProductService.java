package com.kafka.productmicroservice.service;

import com.kafka.core.entity.ReservedProduct;
import com.kafka.productmicroservice.entity.Product;
import com.kafka.productmicroservice.service.dto.AddToCartDto;
import com.kafka.productmicroservice.service.dto.CreateProductDto;

public interface ProductService {
    String createProductAsync(CreateProductDto createProductDto);
    ReservedProduct reserve(Product desiredProduct, Long orderId);
    String addToCart(AddToCartDto addToCartDto);
    void cancelReservation(Product productToCancel, Long orderId);
}
