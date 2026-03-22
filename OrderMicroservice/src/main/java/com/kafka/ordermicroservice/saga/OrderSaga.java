package com.kafka.ordermicroservice.saga;

import com.kafka.core.command.*;
import com.kafka.core.event.*;
import com.kafka.core.types.OrderStatus;
import com.kafka.ordermicroservice.repository.OrderRepository;
import com.kafka.ordermicroservice.service.OrderHistoryService;
import com.kafka.ordermicroservice.service.OrderHistoryServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = {"orders-events", "products-events", "payments-events"},
        groupId = "order-saga-group")
@AllArgsConstructor
public class OrderSaga {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OrderHistoryService orderHistoryService;
    private final OrderRepository orderRepository;

    @KafkaHandler
    public void handleEvent(@Payload OrderCreatedEvent event) {
        LOGGER.info("OrderCreatedEvent: {}", event);
        ReserveProductCommand command = new ReserveProductCommand(
                event.getProducts(),
                event.getOrderId()
        );
        kafkaTemplate.send("products-commands", command);
        orderHistoryService.add(Long.valueOf(event.getOrderId()), OrderStatus.CREATED);
    }

    @KafkaHandler(isDefault = true)
    public void handleUnknown(Object unknown) {
        LOGGER.warn("Received unknown type: {}", unknown);
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservedEvent event) {
        LOGGER.info("Received ProductReservedEvent {}", event);
        ProcessPaymentCommand processPaymentCommand = new ProcessPaymentCommand(event.getOrderId(),
                event.getReservedProducts());
        kafkaTemplate.send("payments-commands", processPaymentCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentProcessedEvent event) {
        LOGGER.info("Received PaymentProcessedEvent {}", event);
        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(event.getOrderId());
        kafkaTemplate.send("orders-commands", approveOrderCommand);

        // inefficient way to get userId. I can just carry it all the way from the first request.
        ClearCartCommand clearCartCommand = new ClearCartCommand(
                orderRepository.findById(event.getOrderId()).get().getUserId());
        kafkaTemplate.send("products-commands", clearCartCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload OrderApprovedEvent event) {
        LOGGER.info("Received OrderApprovedEvent {}", event);
        orderHistoryService.add(event.getOrderId(), OrderStatus.APPROVED);
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentFailedEvent event){
        LOGGER.info("Received PaymentFailedEvent {}", event);
        CancelProductReservationCommand cancelProductReservationCommand =
                new CancelProductReservationCommand(event.getOrderId(), event.getProducts());
        kafkaTemplate.send("products-commands", cancelProductReservationCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservationCancelledEvent event){
        LOGGER.info("Received ProductReservationCancelledEvent {}", event);
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(event.getOrderId());
        kafkaTemplate.send("orders-commands", rejectOrderCommand);
        orderHistoryService.add(event.getOrderId(), OrderStatus.REJECTED);
    }
}
