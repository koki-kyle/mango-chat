package io.koki.mangochat.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;
    private final User owner;
    private final String body;
    private final LocalDateTime timestamp;
}
