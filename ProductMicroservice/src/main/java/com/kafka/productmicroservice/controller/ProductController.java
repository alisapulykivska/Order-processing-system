package com.kafka.productmicroservice.controller;


import com.kafka.productmicroservice.entity.Product;
import com.kafka.productmicroservice.service.ProductService;
import com.kafka.productmicroservice.service.ProductServiceImpl;
import com.kafka.productmicroservice.service.dto.AddToCartDto;
import com.kafka.productmicroservice.service.dto.CheckoutDto;
import com.kafka.productmicroservice.service.dto.CreateProductDto;
import com.kafka.productmicroservice.service.dto.RemoveFromCartDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@Log4j2
@AllArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    //@PreAuthorize("hasRole('admin')")
    @PostMapping
    public ResponseEntity<String> createProductAsync(@Valid @RequestBody CreateProductDto createProductDto){
        String productId = productService.createProductAsync(createProductDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@Valid @RequestBody AddToCartDto addToCartDto){
        String userId = productService.addToCart(addToCartDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/removeFromCart")
    public ResponseEntity<HttpStatus> removeFromCart(@Valid @RequestBody RemoveFromCartDto removeFromCartDto){
        productService.removeProductFromCart(removeFromCartDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/checkout")
    public ResponseEntity<HttpStatus> checkout(@Valid @RequestBody CheckoutDto checkoutDto){
        productService.checkout(checkoutDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.getAllProducts());
    }

}
