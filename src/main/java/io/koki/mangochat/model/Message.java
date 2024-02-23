package io.koki.mangochat.model;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;
    private final String owner;
    private final String body;
    private final String message;

    public Message(String owner, String body) {
        this.owner = owner;
        this.body = body;
        this.message = String.format("%s: %s%n", owner, body);
    }
}
