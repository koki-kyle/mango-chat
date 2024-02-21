package io.koki.mangochat.networking;

import io.koki.mangochat.model.User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TCPMangoClient implements MangoClient {
    private Socket serverSocket = null;

    @Override
    public boolean setUpServer(InetAddress address, int port) {
        boolean successful = false;

        try {
            serverSocket = new Socket(address, port);

            System.out.printf("TCP Client connected to %s:%d%n", address.getCanonicalHostName(), port);
        } catch (IOException e) {
            System.err.println("could not connect to the server: " + e.getMessage());
        }

        return successful;
    }

    @Override
    public boolean authenticate(User user) {
        return true;
    }

    @Override
    public void fetchMessages() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void disconnect() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignore) {
        } finally {
            System.out.println("TCP client disconnected");
        }
    }
}
