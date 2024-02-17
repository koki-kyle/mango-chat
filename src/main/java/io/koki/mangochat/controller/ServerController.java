package io.koki.mangochat.controller;

import io.koki.mangochat.model.DatabaseManager;
import io.koki.mangochat.networking.Server;

public class ServerController {
    private final Server server;
    private final DatabaseManager databaseManager;

    public ServerController(Server server, DatabaseManager databaseManager) {
        this.server = server;
        this.databaseManager = databaseManager;
    }

    public void startServer(int port) {
        server.startServer(port);
    }

    public void stopServer() {
        server.stopServer();
    }
}
