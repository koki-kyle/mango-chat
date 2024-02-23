package io.koki.mangochat.networking.tcp;

import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Getter
public class Connection {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

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
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignore) {}
    }
}
