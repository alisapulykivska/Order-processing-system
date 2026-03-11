package com.kafka.ordermicroservice.config;

import com.kafka.core.OrderCreatedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Autowired
    Environment environment;

    Map<String, Object> producerConfigs(){
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.bootstrap-servers"));
        config.put(ProducerConfig.ACKS_CONFIG, environment.getProperty("spring.kafka.producer.acks"));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, environment.getProperty("spring.kafka.producer.key-serializer"));
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, environment.getProperty("spring.kafka.producer.value-serializer"));
        config.put(ProducerConfig.LINGER_MS_CONFIG, environment.getProperty("spring.kafka.producer.linger.ms"));
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, environment.getProperty("spring.kafka.producer.delivery.timeout.ms"));
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, environment.getProperty("spring.kafka.producer.request.timeout.ms"));
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, environment.getProperty("spring.kafka.producer.max.in.flight.requests.per.connection"));
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
