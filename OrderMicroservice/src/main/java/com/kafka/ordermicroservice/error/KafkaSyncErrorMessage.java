package com.kafka.ordermicroservice.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class KafkaSyncErrorMessage {
    private Date timestamp;
    private String message;
}
