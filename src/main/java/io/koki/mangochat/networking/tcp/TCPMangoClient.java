package io.koki.mangochat.networking.tcp;

import io.koki.mangochat.model.Message;
import io.koki.mangochat.networking.MangoClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPMangoClient extends MangoClient {
    @Override
    public boolean login(String username) {
        InetAddress address = InetAddress.getLoopbackAddress();
        boolean successful = false;
        int port = 1234;

        try {
            conn = new Connection(new Socket(address, port));

            System.out.printf("TCP Client login into %s:%d%n", address.getHostAddress(), port);

            conn.getOut().writeUTF(username);
            conn.getOut().flush();

            successful = conn.getIn().readBoolean();

            if (successful) {
                System.out.println("connected successfully");
            } else {
                System.out.println("failed to login");
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

    @Override
    public void sendMessage(Message message) {
        try {
            conn.getOut().writeObject(message);
            conn.getOut().flush();
        } catch (IOException e) {
            disconnect();
        }
    }

    @Override
    public Message fetchMessage() throws IOException, ClassNotFoundException {
        Message message = null;

        message = (Message) conn.getIn().readObject();

        return message;
    }

    @Override
    public List<Message> fetchChatHistory() {
        List<Message> messages = new ArrayList<>();
        int size;

        try {
            size = conn.getIn().readInt();

            for (int i = 0; i < size; i++) {
                messages.add((Message) conn.getIn().readObject());
            }
        } catch (IOException e) {
            disconnect();
        } catch (ClassNotFoundException ignore) {}

        return messages;
    }
}
