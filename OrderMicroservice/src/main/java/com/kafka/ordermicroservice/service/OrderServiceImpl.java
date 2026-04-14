package com.kafka.ordermicroservice.service;

import com.kafka.core.command.CreateOrderCommand;
import com.kafka.core.dto.GetAmountOfOrderedByProductDto;
import com.kafka.core.event.OrderApprovedEvent;
import com.kafka.core.event.OrderCreatedEvent;
import com.kafka.core.exception.order.OrderCreationException;
import com.kafka.core.exception.order.OrderNotFoundException;
import com.kafka.core.types.OrderStatus;
import com.kafka.ordermicroservice.entity.Order;
import com.kafka.ordermicroservice.entity.OrderItem;
import com.kafka.ordermicroservice.repository.OrderItemRepository;
import com.kafka.ordermicroservice.repository.OrderRepository;

import com.kafka.core.dto.OrderItemDto;
import com.kafka.ordermicroservice.service.dto.OrderDetailsDto;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private KafkaTemplate<String, Object> kafkaTemplate; // default wrapper
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    private Long createOrder(CreateOrderCommand createOrderCommand) {
        Order order = new Order();
        order.setUserId(createOrderCommand.userId());
        orderRepository.save(order);

        for (OrderItemDto orderItemDto : createOrderCommand.orderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(orderItemDto.productId());
            orderItem.setProductName(orderItemDto.productName());
            orderItem.setProductPrice(orderItemDto.productPrice());
            orderItem.setQuantity(orderItemDto.quantity());
            orderItem.setOrder(order);

            orderItemRepository.save(orderItem);
        }
        return order.getId();
    }


    @Transactional
    public String createOrderAsync(CreateOrderCommand createOrderCommand) {

        Long orderId = createOrder(createOrderCommand);
        HashMap<Long, Integer> products = new HashMap<>();
        for(OrderItemDto orderItem : createOrderCommand.orderItems()){
            products.put(orderItem.productId(), orderItem.quantity());
        }


        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                orderId,
                createOrderCommand.userId(),
                products
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
                throw new OrderCreationException("Failed to send order created event", exception);
            } else {
                LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
                LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
                LOGGER.info("Offset: {}", result.getRecordMetadata().offset());
            }
        });

        LOGGER.info("Return: {}", orderId);

        return orderId.toString();
    }

    /*@Transactional
    public String createOrderSync(CreateOrderDto createOrderDto) throws ExecutionException, InterruptedException {

        Long orderId = createOrder(createOrderDto);
        HashMap<Long, Integer> products = new HashMap<>();
        for(OrderItemDto orderItem : createOrderDto.getOrderItems()) {
            if (orderItem.getProductId() != null) {
                products.put(orderItem.getProductId(), orderItem.getQuantity());
            } else {
                LOGGER.warn("Skipping item with null productId: {}", orderItem);
            }
        }


        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                orderId,
                createOrderDto.getUserId(),
                products
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
    }*/

    @Override
    public void approveOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        order.setStatus(OrderStatus.APPROVED);
        orderRepository.save(order);
        OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(orderId);
        kafkaTemplate.send("orders-events", orderApprovedEvent);
    }

    @Override
    public void rejectOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        order.setStatus(OrderStatus.REJECTED);
        orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public OrderDetailsDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        //List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        return new OrderDetailsDto(orderId,
                order.getUserId(),
                order.getItems(),
                order.getStatus());
    }

    public List<GetAmountOfOrderedByProductDto> getAmountOfOrderedByProduct(Long productId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByProductId(productId);

        Map<Long, Long> grouped = orderItems.stream()
                .collect(Collectors.groupingBy(
                        oi -> oi.getOrder().getId(),
                        Collectors.summingLong(OrderItem::getQuantity)
                ));

        return grouped.entrySet().stream()
                .map(entry -> new GetAmountOfOrderedByProductDto(entry.getKey(), entry.getValue()))
                .toList();
    }


}