package io.koki.mangochat.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPServer implements Server {
    private volatile boolean running = false;

    @Override
    public void startServer(int port) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void stopServer() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void handleClient(Socket clientSocket) {
        String username = String.format("%s:%d", clientSocket.getInetAddress().getCanonicalHostName(), clientSocket.getPort());

        new Thread(() -> {
            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                String message;

                while ((message = reader.readLine()) != null) {
                    System.out.printf("received message from %s > %s%n", username, message);
                    
                    writer.printf("server received your message: %s%n", message);
                }
            } catch (IOException ignore) {
            } finally {
                System.out.printf("client disconnected: %s%n", username);
            }
        }).start();
    }
}
