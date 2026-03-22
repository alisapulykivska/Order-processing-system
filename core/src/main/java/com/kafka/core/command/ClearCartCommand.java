package com.kafka.core.command;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClearCartCommand {
    private Long userId;
}
