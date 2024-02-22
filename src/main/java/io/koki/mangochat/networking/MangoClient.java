package io.koki.mangochat.networking;

import io.koki.mangochat.model.User;

import java.net.InetAddress;

public interface MangoClient {
    void setUpServer(InetAddress address, int port);
    boolean authenticate(User user);
    boolean register(User user);
    void fetchMessages();
    void disconnect();
}
