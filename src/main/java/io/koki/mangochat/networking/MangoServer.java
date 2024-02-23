package io.koki.mangochat.networking;

import io.koki.mangochat.networking.tcp.ActiveUser;
import io.koki.mangochat.model.Message;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public abstract class MangoServer {
    protected ServerSocket serverSocket = null;
    protected List<ActiveUser> activeUsers = new ArrayList<>();
    protected List<Message> chatHistory = new ArrayList<>();
    protected volatile boolean running = false;

    public abstract void startServer(int port);

    public abstract void stopServer();
}
