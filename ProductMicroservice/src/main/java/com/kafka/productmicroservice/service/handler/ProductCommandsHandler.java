package com.kafka.productmicroservice.service.handler;

import com.kafka.core.command.CancelProductReservationCommand;
import com.kafka.core.command.ClearCartCommand;
import com.kafka.core.command.ReserveProductCommand;
import com.kafka.core.entity.ReservedProduct;
import com.kafka.core.event.ProductReservationCancelledEvent;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@KafkaListener(topics = "products-commands")
@AllArgsConstructor
public class ProductCommandsHandler {
    private static final Logger log = LoggerFactory.getLogger(ProductCommandsHandler.class);
    private final ProductService productService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaHandler
    public void handleCommand(@Payload ReserveProductCommand command) {
        try {
            List<ReservedProduct> reservedProducts = new ArrayList<>();
            log.info("Handling ReserveProductCommand {}", command);
            for(Map.Entry entry : command.products().entrySet()) {
                Long productId = (Long) entry.getKey();
                Integer quantity = (Integer) entry.getValue();

                Product desiredProduct = new Product(productId, quantity);
                ReservedProduct reservedProduct = productService.reserve(desiredProduct, command.orderId());

                log.info("Reserving product {}", productId);
                reservedProducts.add(reservedProduct);
            }
            ProductReservedEvent productReservedEvent = new ProductReservedEvent(command.orderId(), reservedProducts);
            kafkaTemplate.send("products-events", productReservedEvent);
        } catch (Exception e){
            log.info("Exception!");
            log.error(e.getMessage());
            ProductReservationFailedEvent productReservationFailedEvent = new ProductReservationFailedEvent(
                    command.orderId(), command.products());
            kafkaTemplate.send("products-events", productReservationFailedEvent);
        }
    }

    @KafkaHandler
    public void handleCommand(@Payload CancelProductReservationCommand command){
        log.info("Handling CancelProductReservationCommand {}", command);
        productService.cancelReservation(command.products(), command.orderId());
        List<Long> productIds = command.products().stream().map(ReservedProduct::productId)
                .collect(Collectors.toList());

        ProductReservationCancelledEvent productReservationCancelledEvent =
                new ProductReservationCancelledEvent(productIds, command.orderId());
        kafkaTemplate.send("products-events", productReservationCancelledEvent);
    }

    @KafkaHandler
    public void handleCommand(@Payload ClearCartCommand command){
        log.info("Handling ClearCartCommand {}", command);
        productService.clearCart(command.userId());
    }
}
