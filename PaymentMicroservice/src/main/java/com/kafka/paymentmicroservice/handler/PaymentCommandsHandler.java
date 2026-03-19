package com.kafka.paymentmicroservice.handler;

import com.kafka.core.command.ProcessPaymentCommand;
import com.kafka.core.event.PaymentFailedEvent;
import com.kafka.core.event.PaymentProcessedEvent;
import com.kafka.core.exception.CreditCardProcessorUnavailableException;
import com.kafka.paymentmicroservice.entity.Payment;
import com.kafka.paymentmicroservice.service.PaymentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "payments-commands")
@AllArgsConstructor
public class PaymentCommandsHandler {
    private final PaymentService paymentService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaHandler
    public void handleCommand(@Payload ProcessPaymentCommand command) {
        logger.info("Processing ProcessPaymentCommand {}", command);
        try {
            Payment payment = new Payment(command.getOrderId(),
                    command.getProductId(),
                    command.getProductPrice(),
                    command.getProductQuantity());
            Payment processedPayment = paymentService.process(payment);
            PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(processedPayment.getOrderId(),
                    processedPayment.getId());
            kafkaTemplate.send("payments-events", paymentProcessedEvent);
        } catch (CreditCardProcessorUnavailableException e) {
            logger.error(e.getLocalizedMessage(), e);
            PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(command.getOrderId(),
                    command.getProductId(),
                    command.getProductQuantity());
            kafkaTemplate.send("payments-events",paymentFailedEvent);
        }
    }
    /*
    Used when testing compensation transaction
    @KafkaHandler
    public void handleCommand(@Payload ProcessPaymentCommand command) {
        logger.info("Sending payment failed");
        PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(command.getOrderId(),
                command.getProductId(),
                command.getProductQuantity());
        kafkaTemplate.send("payments-events",paymentFailedEvent);
    }*/
}
