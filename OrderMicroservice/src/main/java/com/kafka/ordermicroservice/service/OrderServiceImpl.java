package com.kafka.ordermicroservice.service;

import com.kafka.core.OrderCreatedEvent;
import com.kafka.ordermicroservice.entity.Order;
import com.kafka.ordermicroservice.entity.OrderItem;
import com.kafka.ordermicroservice.repository.OrderItemRepository;
import com.kafka.ordermicroservice.repository.OrderRepository;
import com.kafka.ordermicroservice.service.dto.CreateOrderDto;

import com.kafka.ordermicroservice.service.dto.OrderItemDto;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate; // default wrapper
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public String createOrderAsync(CreateOrderDto createOrderDto) {

        Order order = new Order();
        order.setUserId(createOrderDto.getUserId());
        orderRepository.save(order);

        for(OrderItemDto orderItemDto : createOrderDto.getOrderItems()){
            OrderItem orderItem =  new OrderItem();
            orderItem.setProductId(orderItemDto.getProductId());
            orderItem.setProductName(orderItemDto.getProductName());
            orderItem.setProductPrice(orderItemDto.getProductPrice());
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setOrder(order);

            orderItemRepository.save(orderItem);
        }

        String orderId = order.getId().toString();

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                orderId,
                createOrderDto.getOrderItems().get(0).getProductName(),
                createOrderDto.getOrderItems().get(0).getProductPrice(),
                createOrderDto.getOrderItems().get(0).getQuantity()
                );

        ProducerRecord<String, OrderCreatedEvent> record = new ProducerRecord<>(
                "order-created-events-topic",
                orderId,
                orderCreatedEvent
        );

        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());

        CompletableFuture<SendResult<String, OrderCreatedEvent>> future = kafkaTemplate.send(record);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                LOGGER.error(exception.getMessage());
            } else {
                LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
                LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
                LOGGER.info("Offset: {}", result.getRecordMetadata().offset());
            }
        });

        LOGGER.info("Return: {}", orderId);

        return orderId;
    }

    @Transactional
    public String createOrderSync(CreateOrderDto createOrderDto) throws ExecutionException, InterruptedException {
        // todo: save to db and use id given from it
        String orderId = UUID.randomUUID().toString();

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                orderId,
                createOrderDto.getOrderItems().get(0).getProductName(),
                createOrderDto.getOrderItems().get(0).getProductPrice(),
                createOrderDto.getOrderItems().get(0).getQuantity()
        );

        ProducerRecord<String, OrderCreatedEvent> record = new ProducerRecord<>(
                "order-created-events-topic",
                orderId,
                orderCreatedEvent
        );

        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());

        SendResult<String, OrderCreatedEvent> result = kafkaTemplate.send(
                record).get();


        LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
        LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
        LOGGER.info("Offset: {}", result.getRecordMetadata().offset());

        return orderId;
    }

}
