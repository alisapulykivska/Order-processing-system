package com.shipping.mock_shipping_service.controller;

import com.shipping.mock_shipping_service.dto.DistanceResponse;
import com.shipping.mock_shipping_service.dto.MockGetDistanceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/shipping")
public class ShippingController {
    private static final Logger log = LoggerFactory.getLogger(ShippingController.class);

    @GetMapping("/")
    public ResponseEntity<DistanceResponse> getDistance(@RequestBody MockGetDistanceDto getDistanceDto){
        log.info("Received request: {}", getDistanceDto);
        Random rand = new Random();
        int dist = rand.nextInt(500);
        return ResponseEntity.ok().body(new DistanceResponse(dist));
    }
}
