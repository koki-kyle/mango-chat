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
        if (!server.isRunning()) {
            new Thread(() -> server.startServer(port)).start();
        } else {
            System.out.println("server is already running");
        }
    }

    public void stopServer() {
        if (server.isRunning()) {
            server.stopServer();
        } else {
            System.out.println("server is already stopped");
        }
    }
}
