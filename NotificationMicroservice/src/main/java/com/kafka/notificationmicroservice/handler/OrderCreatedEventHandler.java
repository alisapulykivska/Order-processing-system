package com.kafka.notificationmicroservice.handler;

import com.kafka.core.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "order-created-events-topic")
public class OrderCreatedEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @KafkaHandler // Map by input DTO
    public void handler(OrderCreatedEvent orderCreatedEvent) {
        LOGGER.info("Received product created event: " + orderCreatedEvent.getOrderId());
    }
}
