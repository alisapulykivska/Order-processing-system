package com.kafka.ordermicroservice.service;

import com.kafka.core.event.OrderApprovedEvent;
import com.kafka.core.event.OrderCreatedEvent;
import com.kafka.core.types.OrderStatus;
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
import org.springframework.util.Assert;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private KafkaTemplate<String, Object> kafkaTemplate; // default wrapper
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    private Long createOrder(CreateOrderDto createOrderDto) {
        Order order = new Order();
        order.setUserId(createOrderDto.getUserId());
        orderRepository.save(order);

        for (OrderItemDto orderItemDto : createOrderDto.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(orderItemDto.getProductId());
            orderItem.setProductName(orderItemDto.getProductName());
            orderItem.setProductPrice(orderItemDto.getProductPrice());
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setOrder(order);

            orderItemRepository.save(orderItem);
        }
        return order.getId();
    }


    @Transactional
    public String createOrderAsync(CreateOrderDto createOrderDto) {

        Long orderId = createOrder(createOrderDto);

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                orderId,
                createOrderDto.getUserId(),
                createOrderDto.getOrderItems().get(0).getProductId(),
                createOrderDto.getOrderItems().get(0).getQuantity()
        );

        ProducerRecord<String, Object> record = new ProducerRecord<>(
                "orders-events",
                orderId.toString(),
                orderCreatedEvent
        );

        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(record);

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

        return orderId.toString();
    }

    @Transactional
    public String createOrderSync(CreateOrderDto createOrderDto) throws ExecutionException, InterruptedException {

        Long orderId = createOrder(createOrderDto);

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                orderId,
                createOrderDto.getUserId(),
                createOrderDto.getOrderItems().get(0).getProductId(),
                createOrderDto.getOrderItems().get(0).getQuantity()
        );

        ProducerRecord<String, Object> record = new ProducerRecord<>(
                "orders-events",
                orderId.toString(),
                orderCreatedEvent
        );

        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());

        SendResult<String, Object> result = kafkaTemplate.send(
                record).get();


        LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
        LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
        LOGGER.info("Offset: {}", result.getRecordMetadata().offset());

        return orderId.toString();
    }

    @Override
    public void approveOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        Assert.notNull(order, "No order is found with id " + orderId + " in the database table");
        order.setStatus(OrderStatus.APPROVED);
        orderRepository.save(order);
        OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(orderId);
        kafkaTemplate.send("orders-events", orderApprovedEvent);
    }
}