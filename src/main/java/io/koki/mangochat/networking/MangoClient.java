package io.koki.mangochat.networking;

import io.koki.mangochat.model.Message;
import io.koki.mangochat.networking.tcp.Connection;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public abstract class MangoClient {
    protected Connection conn;

    public void disconnect() {
        if (Objects.nonNull(conn)) {
            conn.disconnect();
            System.out.println("client disconnected");
        }
    }

    public abstract boolean login(String username);

    public abstract void sendMessage(Message message);

    public abstract Message fetchMessage() throws IOException, ClassNotFoundException;

    public abstract List<Message> fetchChatHistory();
}
