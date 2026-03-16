package com.kafka.productmicroservice.controller;


import com.kafka.productmicroservice.service.ProductService;
import com.kafka.productmicroservice.service.ProductServiceImpl;
import com.kafka.productmicroservice.service.dto.AddToCartDto;
import com.kafka.productmicroservice.service.dto.CreateProductDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@Log4j2
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> createProductAsync(@RequestBody CreateProductDto createProductDto){
        String productId = productService.createProductAsync(createProductDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@RequestBody AddToCartDto addToCartDto){
        String userId = productService.addToCart(addToCartDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }

}
