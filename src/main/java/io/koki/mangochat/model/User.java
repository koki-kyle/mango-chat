package io.koki.mangochat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 20L;
    private String username;
    private String password;
}
