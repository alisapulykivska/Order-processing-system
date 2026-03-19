package com.kafka.ordermicroservice.service.handler;

import com.kafka.core.command.ApproveOrderCommand;
import com.kafka.core.command.RejectOrderCommand;
import com.kafka.ordermicroservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics="orders-commands")
@AllArgsConstructor
public class OrderCommandsHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderCommandsHandler.class);
    private final OrderService orderService;

    @KafkaHandler
    public void handleCommand(@Payload ApproveOrderCommand approveOrderCommand) {
        orderService.approveOrder(approveOrderCommand.getOrderId());
    }
    @KafkaHandler
    public void handleCommand(@Payload RejectOrderCommand rejectOrderCommand) {
        log.info("Rejecting order");
        orderService.rejectOrder(rejectOrderCommand.getOrderId());
    }
}
