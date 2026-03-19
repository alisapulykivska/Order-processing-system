package com.kafka.ordermicroservice.saga;

import com.kafka.core.command.ApproveOrderCommand;
import com.kafka.core.command.ProcessPaymentCommand;
import com.kafka.core.command.ReserveProductCommand;
import com.kafka.core.event.OrderApprovedEvent;
import com.kafka.core.event.OrderCreatedEvent;
import com.kafka.core.event.PaymentProcessedEvent;
import com.kafka.core.event.ProductReservedEvent;
import com.kafka.core.types.OrderStatus;
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

    @KafkaHandler
    public void handleEvent(@Payload OrderCreatedEvent event) {
        ReserveProductCommand command = new ReserveProductCommand(
                event.getProductId(),
                event.getProductQuantity(),
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
                event.getProductId(), event.getProductPrice(), event.getProductQuantity());
        kafkaTemplate.send("payments-commands", processPaymentCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentProcessedEvent event) {
        LOGGER.info("Received PaymentProcessedEvent {}", event);
        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(event.getOrderId());
        kafkaTemplate.send("orders-commands", approveOrderCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload OrderApprovedEvent event) {
        LOGGER.info("Received OrderApprovedEvent {}", event);
        orderHistoryService.add(event.getOrderId(), OrderStatus.APPROVED);
    }
}
