package io.koki.mangochat.networking;

import io.koki.mangochat.model.User;

public interface Client {
    void authenticate(User user);
    void fetchMessages();
    void disconnect();
}
