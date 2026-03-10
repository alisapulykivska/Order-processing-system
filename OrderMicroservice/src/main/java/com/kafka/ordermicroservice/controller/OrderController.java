package com.kafka.ordermicroservice.controller;

import com.kafka.ordermicroservice.error.KafkaSyncErrorMessage;
import com.kafka.ordermicroservice.service.dto.CreateOrderDto;
import com.kafka.ordermicroservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/sync")
    public ResponseEntity<Object> createOrderSync(@RequestBody CreateOrderDto createOrderDto){
        String orderId = null;
        try {
            orderId = orderService.createOrderSync(createOrderDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new KafkaSyncErrorMessage(new Date(), e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }

    @PostMapping("/async")
    public ResponseEntity<String> createOrderAsync(@RequestBody CreateOrderDto createOrderDto){
        String orderId = orderService.createOrderAsync(createOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }

}
