package io.koki.mangochat.networking;

import io.koki.mangochat.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TCPMangoClient implements MangoClient {
    private Socket serverSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private InetAddress address;
    private int port;

    @Override
    public void setUpServer(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public boolean authenticate(User user) {
        return connectToServer(user, true);
    }

    @Override
    public boolean register(User user) {
        return connectToServer(user, false);
    }

    @Override
    public void fetchMessages() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void disconnect() {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException ignore) {}

        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException ignore) {}

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignore) {
        } finally {
            System.out.println("TCP client disconnected");
        }
    }

    private boolean connectToServer(User user, boolean isUserLoginIn) {
        boolean successful = false;

        serverSocket = null;
        in = null;
        out = null;

        try {
            serverSocket = new Socket(address, port);
            out = new ObjectOutputStream(serverSocket.getOutputStream());
            in = new ObjectInputStream(serverSocket.getInputStream());

            System.out.printf("TCP Client %s to %s:%d%n",
                    isUserLoginIn ? "authenticating"
                            : "registering",
                    address.getHostAddress(),
                    port);

            out.writeObject(user);
            out.writeBoolean(isUserLoginIn);
            out.flush();

            successful = in.readBoolean();

            if (successful) {
                System.out.println("connected successfully");
            } else {
                System.out.printf("failed to %s%n", isUserLoginIn ? "authenticate" : "register");
            }
        } catch (IOException e) {
            System.err.println("could not connect to server: " + e.getMessage());
        } finally {
            if (!successful) {
                disconnect();
            }
        }

        return successful;
    }
}
