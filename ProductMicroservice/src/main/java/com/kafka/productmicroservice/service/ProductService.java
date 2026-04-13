package com.kafka.productmicroservice.service;

import com.kafka.core.entity.ReservedProduct;
import com.kafka.productmicroservice.entity.Product;
import com.kafka.productmicroservice.service.dto.*;

import java.util.List;

public interface ProductService {
    String createProductAsync(CreateProductDto createProductDto);
    ReservedProduct reserve(Product desiredProduct, Long orderId);
    String addToCart(AddToCartDto addToCartDto);
    void cancelReservation(List<ReservedProduct> products, Long orderId);
    void checkout(CheckoutDto checkoutDto);
    void clearCart(Long orderId);
    void removeProductFromCart(RemoveFromCartDto removeFromCartDto);
    List<Product> getAllProducts();
    ProductDetailsDto getProductById(Long productId);
    void deleteProduct(Long productId);
    Product updateProduct(Long productId, UpdateProductDto updateProductDto);
}
