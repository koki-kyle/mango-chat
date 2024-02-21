package io.koki.mangochat.networking;

import io.koki.mangochat.model.User;

import java.net.InetAddress;

public class UDPMangoClient implements MangoClient {
    @Override
    public boolean setUpServer(InetAddress address, int port) {
        return false;
    }

    @Override
    public boolean authenticate(User user) {
        return false;
    }

    @Override
    public void fetchMessages() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void disconnect() {
        throw new RuntimeException("not implemented");
    }
}
