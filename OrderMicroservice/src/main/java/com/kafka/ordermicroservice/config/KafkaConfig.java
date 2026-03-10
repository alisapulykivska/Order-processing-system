package com.kafka.ordermicroservice.config;

import com.kafka.core.OrderCreatedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.producer.acks}")
    private String acks;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
    private String deliveryTimeoutMs;

    @Value("${spring.kafka.producer.properties.linger.ms}")
    private String lingerMs;

    @Value("${spring.kafka.producer.properties.request.timeout.ms}")
    private String requestTimeoutMs;

    @Value("${spring.kafka.producer.properties.max.in.flight.requests.per.connection}")
    private String maxInFlightRequestsPerConnection;

    Map<String, Object> producerConfigs(){
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.ACKS_CONFIG, acks);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        config.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs);
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequestsPerConnection);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // idempotence

        return config;
    }

    @Bean
    ProducerFactory<String, OrderCreatedEvent> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate(){
        return new KafkaTemplate<String, OrderCreatedEvent>(producerFactory());
    }

    @Bean
    NewTopic newTopic(){
        return TopicBuilder.name("order-created-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas","2")) // leader + 1 replica
                .build();
    }
}
