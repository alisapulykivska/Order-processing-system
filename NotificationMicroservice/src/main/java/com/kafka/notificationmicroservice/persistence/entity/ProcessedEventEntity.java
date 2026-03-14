package com.kafka.notificationmicroservice.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="processed_events")
@NoArgsConstructor
@Getter
@Setter
public class ProcessedEventEntity {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String messageId;

    @Column(nullable = false)
    private String orderId;

    public ProcessedEventEntity(String messageId, String orderId) {
        this.messageId = messageId;
        this.orderId = orderId;
    }
}
