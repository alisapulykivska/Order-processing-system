package com.kafka.core.command;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RejectOrderCommand {
    private Long orderId;
}
