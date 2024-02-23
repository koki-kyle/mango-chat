package io.koki.mangochat.networking.tcp;

import io.koki.mangochat.model.Message;
import io.koki.mangochat.networking.MangoServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Objects;

public class TCPMangoServer extends MangoServer {
    @Override
    public void startServer(final int port) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                running = true;

                System.out.println("TCP Server started on port " + port);

                while (true) {
                    Connection conn = null;
                    boolean successful = false;

                    try {
                        conn = new Connection(serverSocket.accept());

                        String username = conn.getIn().readUTF();

                        System.out.printf("%s:%d with username '%s' login in%n",
                                conn.getSocket().getInetAddress().getHostAddress(),
                                conn.getSocket().getPort(),
                                username);

                        successful = activeUsers.stream()
                                .filter(activeUser -> activeUser.getUsername().equalsIgnoreCase(username))
                                .findFirst()
                                .isEmpty();

                        conn.getOut().writeBoolean(successful);
                        conn.getOut().flush();

                        if (successful) {
                            Message message = new Message("server", String.format("'%s' has enter the chat", username));

                            System.out.printf("'%s' connected successfully%n", username);

                            ActiveUser newUser = new ActiveUser(conn, username);

                            chatHistory.add(message);
                            broadcast(message);

                            activeUsers.add(newUser);

                            handleClient(newUser);
                        } else {
                            System.out.printf("%s failed to login%n", username);
                        }
                    } catch (SocketException ignore) {
                    } catch (IOException e) {
                        System.err.println("could not connect with client: " + e.getMessage());
                    } finally {
                        if (!successful && Objects.nonNull(conn)) {
                            conn.disconnect();
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("could not start the server: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public void stopServer() {
        if (running) {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException ignore) {
            } finally {
                System.out.println("TCP Server stopped");
                running = false;
            }
        } else {
            System.out.println("already stopped");
        }
    }

    private void handleClient(ActiveUser user) {
        Connection conn = user.getConn();
        ObjectOutputStream out = conn.getOut();
        ObjectInputStream in = conn.getIn();

        new Thread(() -> {
            try {
                out.writeInt(chatHistory.size());
                out.flush();

                for (Message message : chatHistory) {
                    out.writeObject(message);
                    out.flush();
                }

                do {
                    Message message = (Message) in.readObject();

                    chatHistory.add(message);
                    broadcast(message);

                } while (true);
            } catch (IOException ignore) {
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                Message message = new Message("server", String.format("'%s' has left the chat", user.getUsername()));

                System.out.printf("client disconnected: %s%n", user.getUsername());

                conn.disconnect();

                chatHistory.add(message);
                broadcast(message);

                activeUsers.remove(user);
            }
        }).start();
    }

    private void broadcast(Message message) {
        activeUsers.forEach(activeUser -> {
            if (!activeUser.getUsername().equalsIgnoreCase(message.getOwner())) {
                try {
                    activeUser.getConn().getOut().writeObject(message);
                    activeUser.getConn().getOut().flush();
                } catch (IOException e) {
                    activeUser.getConn().disconnect();
                }
            }
        });
    }
}
