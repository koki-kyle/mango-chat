package io.koki.mangochat.controller;

import io.koki.mangochat.model.DatabaseManager;
import io.koki.mangochat.networking.MangoServer;

public class ServerController {
    private final MangoServer mangoServer;
    private final DatabaseManager databaseManager;

    public ServerController(MangoServer mangoServer, DatabaseManager databaseManager) {
        this.mangoServer = mangoServer;
        this.databaseManager = databaseManager;

        mangoServer.setUserAuthenticationPredicate(user -> databaseManager
                .getUserByUsernameAndPassword(user.getUsername(), user.getPassword())
                .isPresent());

        mangoServer.setUserRegistrationPredicate(user -> {
            boolean successful = databaseManager.getUserByUsername(user.getUsername()).isEmpty();

            if (successful) {
                databaseManager.addUser(user);
            }
            return successful;
        });
    }

    public void startServer(int port) {
        if (!mangoServer.isRunning()) {
            new Thread(() -> mangoServer.startServer(port)).start();
        } else {
            System.out.println("server is already running");
        }
    }

    public void stopServer() {
        if (mangoServer.isRunning()) {
            mangoServer.stopServer();
        } else {
            System.out.println("server is already stopped");
        }
    }
}
