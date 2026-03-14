    package com.kafka.notificationmicroservice.handler;

    import com.kafka.core.OrderCreatedEvent;
    import com.kafka.notificationmicroservice.exception.NonRetryableException;
    import com.kafka.notificationmicroservice.exception.RetryableException;
    import com.kafka.notificationmicroservice.persistence.entity.ProcessedEventEntity;
    import com.kafka.notificationmicroservice.persistence.repository.ProcessedEventRepository;
    import lombok.AllArgsConstructor;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.dao.DataIntegrityViolationException;
    import org.springframework.kafka.annotation.KafkaHandler;
    import org.springframework.kafka.annotation.KafkaListener;
    import org.springframework.kafka.support.KafkaHeaders;
    import org.springframework.messaging.handler.annotation.Header;
    import org.springframework.messaging.handler.annotation.Payload;
    import org.springframework.stereotype.Component;
    import org.springframework.transaction.annotation.Transactional;

    @Component
    @AllArgsConstructor
    @KafkaListener(topics = "order-created-events-topic", groupId = "order-created-events")
    public class OrderCreatedEventHandler {

        private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

        private ProcessedEventRepository processedEventRepository;

        @KafkaHandler // Map by input DTO
        @Transactional
        public void handler(@Payload OrderCreatedEvent orderCreatedEvent,
                            @Header("messageId") String messageId,
                            @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
            LOGGER.info("Received product created event: " + orderCreatedEvent.getOrderId());

            ProcessedEventEntity processedEventEntity = processedEventRepository.findByMessageId(messageId);

            if(processedEventEntity != null) {
                LOGGER.info("Dublicate message id: {}", orderCreatedEvent.getOrderId());
                return;
            }

            // logic

            // after logic

            try{
                processedEventRepository.save(new ProcessedEventEntity(messageId, orderCreatedEvent.getOrderId()));
            } catch (DataIntegrityViolationException e) {
                LOGGER.error(e.getMessage());
                throw new NonRetryableException(e.getMessage());
            }
        }

        @KafkaHandler(isDefault = true)
        public void handleUnknown(Object unknown) {
            LOGGER.warn("Received unknown type: {}", unknown);
        }
    }
