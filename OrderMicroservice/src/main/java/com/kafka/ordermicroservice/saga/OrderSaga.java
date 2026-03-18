package com.kafka.ordermicroservice.saga;

import com.kafka.core.command.ReserveProductCommand;
import com.kafka.core.event.OrderCreatedEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "order-created-events", groupId = "order-saga-group", containerFactory = "kafkaListenerContainerFactory")
@AllArgsConstructor
public class OrderSaga {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaHandler
    public void handleEvent(@Payload OrderCreatedEvent event) {
        System.out.println("Received OrderCreatedEvent: " + event);

        ReserveProductCommand command = new ReserveProductCommand(
                event.getTitle(),
                event.getQuantity(),
                event.getPrice()
        );
        kafkaTemplate.send("product-commands-events", command);

        // save to orderHistory
    }

    @KafkaHandler(isDefault = true)
    public void handleUnknown(Object unknown) {
        LOGGER.warn("Received unknown type: {}", unknown);
    }
}
