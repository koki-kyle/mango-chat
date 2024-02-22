package io.koki.mangochat.networking;

import io.koki.mangochat.model.Message;
import io.koki.mangochat.model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Predicate;

public class TCPMangoServer implements MangoServer {
    private ServerSocket serverSocket = null;
    private volatile boolean running = false;
    private Predicate<User> userAuthentication = null;

    @Override
    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            running = true;

            System.out.println("TCP Server started on port " + port);

            while (true) {
                Socket clientSocket = null;
                ObjectInputStream in = null;
                ObjectOutputStream out = null;
                boolean authSuccessful = false;

                try {
                    clientSocket = serverSocket.accept();
                    out = new ObjectOutputStream(clientSocket.getOutputStream());
                    in = new ObjectInputStream(clientSocket.getInputStream());

                    User user = (User) in.readObject();

                    System.out.printf("%s:%d with username '%s' authenticating%n", clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort(), user.getUsername());

                    authSuccessful = userAuthentication.test(user);

                    out.writeBoolean(authSuccessful);
                    out.flush();

                    if (authSuccessful) {
                        System.out.printf("'%s' connected successfully%n", user.getUsername());
                        handleClient(clientSocket, out, in, user);
                    } else {
                        System.out.printf("%s failed to authenticate%n", user.getUsername());
                    }
                } catch (SocketException ignore) {
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("could not connect with client: " + e.getMessage());
                } finally {
                    if (!authSuccessful) {
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
                            if (clientSocket != null && !clientSocket.isClosed()) {
                                clientSocket.close();
                            }
                        } catch (IOException ignore) {}
                    }
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

    @Override
    public void setUserAuthenticationPredicate(Predicate<User> predicate) {
        userAuthentication = predicate;
    }

    private void handleClient(Socket client, ObjectOutputStream clientOut, ObjectInputStream clientIn, User user) {
        String username = user.getUsername();

        new Thread(() -> {
            try (client; clientOut; clientIn) {
                do {
                    Message message = (Message) clientIn.readObject();

                    System.out.printf("received message from %s > %s%n", username, message.getBody());

                    clientOut.writeUTF(String.format("server received your message: %s%n", message.getBody()));
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
