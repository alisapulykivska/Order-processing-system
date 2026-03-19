package com.kafka.ordermicroservice.service.handler;

import com.kafka.core.command.ApproveOrderCommand;
import com.kafka.ordermicroservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics="orders-commands")
@AllArgsConstructor
public class OrderCommandsHandler {
    private final OrderService orderService;

    @KafkaHandler
    public void handleCommand(@Payload ApproveOrderCommand approveOrderCommand) {
        orderService.approveOrder(approveOrderCommand.getOrderId());
    }
}
