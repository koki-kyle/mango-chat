package io.koki.mangochat.networking;

import io.koki.mangochat.model.User;

public interface Client {
    boolean authenticate(User user);
    void fetchMessages();
}
