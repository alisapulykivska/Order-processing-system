package com.kafka.ordermicroservice.controller;

import com.kafka.core.dto.GetAmountOfOrderedByProductDto;
import com.kafka.ordermicroservice.entity.Order;
import com.kafka.ordermicroservice.service.OrderService;
import com.kafka.ordermicroservice.service.dto.OrderDetailsDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Log4j2
@AllArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsDto> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<GetAmountOfOrderedByProductDto>> countOfOrderedByProduct(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getAmountOfOrderedByProduct(id));
    }
}
