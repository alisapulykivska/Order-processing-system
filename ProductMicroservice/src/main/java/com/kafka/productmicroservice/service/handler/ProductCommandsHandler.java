package com.kafka.productmicroservice.service.handler;

import com.kafka.core.command.ReserveProductCommand;
import com.kafka.core.event.ProductReservationFailedEvent;
import com.kafka.core.event.ProductReservedEvent;
import com.kafka.productmicroservice.entity.Product;
import com.kafka.productmicroservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "orders-commands")
@AllArgsConstructor
public class ProductCommandsHandler {
    private static final Logger log = LoggerFactory.getLogger(ProductCommandsHandler.class);
    private final ProductService productService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaHandler
    public void handleCommand(@Payload ReserveProductCommand command) {
        try {
            Product desiredProduct = new Product(command.getProductId(), command.getProductQuantity());
            Product reservedProduct = productService.reserve(desiredProduct, command.getOrderId());
            ProductReservedEvent productReservedEvent = new ProductReservedEvent(command.getOrderId(),
                    command.getProductId(),
                    reservedProduct.getPrice(),
                    command.getProductQuantity());
            kafkaTemplate.send("products-events", productReservedEvent);
        } catch (Exception e){
            log.error(e.getMessage());
            ProductReservationFailedEvent productReservationFailedEvent = new ProductReservationFailedEvent(command.getProductId(),
                    command.getOrderId(), command.getProductQuantity());
            kafkaTemplate.send("products-events", productReservationFailedEvent);
        }
    }
}
