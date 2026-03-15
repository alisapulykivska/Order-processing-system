package com.kafka.productmicroservice.service;

import com.kafka.productmicroservice.entity.Product;
import com.kafka.productmicroservice.repository.ProductRepository;
import com.kafka.productmicroservice.service.dto.CreateProductDto;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public String createProductAsync(CreateProductDto createProductDto) {
        Product product = new Product().builder()
                .title(createProductDto.getTitle())
                .price(createProductDto.getPrice())
                .quantity(createProductDto.getQuantity())
                .createdOn(LocalDateTime.now())
                .build();

        productRepository.save(product);

        return "";
    }
}
