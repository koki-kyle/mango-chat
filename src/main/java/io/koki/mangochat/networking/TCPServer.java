package io.koki.mangochat.networking;

import io.koki.mangochat.model.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer implements Server {
    private ServerSocket serverSocket = null;
    private volatile boolean running = false;

    @Override
    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            running = true;

            System.out.println("TCP Server started on port " + port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.printf("client connected:%n\taddress: %s%n\tport: %d%n%n", clientSocket.getInetAddress().getCanonicalHostName(), clientSocket.getPort());

                    // Handle client communication in a separate thread
                    handleClient(clientSocket);
                } catch (SocketException ignore) {
                } catch (IOException e) {
                    System.err.println("could not connect with client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("could not start the server: " + e.getMessage());
        }
    }

    @Override
    public void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignore) {
        } finally {
            System.out.println("TCP Server stopped");
            running = false;
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void handleClient(Socket clientSocket) {
        String username = String.format("%s:%d", clientSocket.getInetAddress().getCanonicalHostName(), clientSocket.getPort());

        new Thread(() -> {
            try (
                    ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                do {
                    Message message = (Message) reader.readObject();

                    System.out.printf("received message from %s > %s%n", message.getOwner().getUsername(), message.getBody());

                    writer.printf("server received your message: %s%n", message.getBody());
                } while (true);
            } catch (IOException ignore) {
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.printf("client disconnected: %s%n", username);
            }
        }).start();
    }
}
