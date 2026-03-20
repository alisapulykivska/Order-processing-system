package com.kafka.core.event;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderApprovedEvent {
    private Long orderId;
}
