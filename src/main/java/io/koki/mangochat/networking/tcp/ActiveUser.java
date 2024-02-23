package io.koki.mangochat.networking.tcp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActiveUser {
    private final Connection conn;
    private String username;
}

