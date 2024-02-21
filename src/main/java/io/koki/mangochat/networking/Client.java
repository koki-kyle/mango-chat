package io.koki.mangochat.networking;

import io.koki.mangochat.model.User;

import java.net.InetAddress;

public interface Client {
    boolean setUpServer(InetAddress address, int port);
    boolean authenticate(User user);
    void fetchMessages();
    void disconnect();
}
