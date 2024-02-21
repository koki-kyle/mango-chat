package io.koki.mangochat;

import io.koki.mangochat.config.AppConfig;
import io.koki.mangochat.config.CommunicationMode;
import io.koki.mangochat.controller.ServerController;
import io.koki.mangochat.model.DatabaseManager;
import io.koki.mangochat.networking.MangoServer;
import io.koki.mangochat.networking.TCPMangoServer;
import io.koki.mangochat.networking.UDPMangoServer;

public class MangoChatServer {
    public static void main(String[] args) {
        // configure the communication mode, either TCP or UDP
        AppConfig.setCommunicationMode(CommunicationMode.TCP);

        // setup initial components
        MangoServer mangoServer = AppConfig.getCommunicationMode() == CommunicationMode.TCP
                ? new TCPMangoServer()
                : new UDPMangoServer();
        DatabaseManager databaseManager = new DatabaseManager();

        // demo data for testing
        databaseManager.demoData();

        ServerController serverController = new ServerController(mangoServer, databaseManager);

        // start the server
        int port = 1234;
        serverController.startServer(port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("shutting down server...");
            serverController.stopServer();
        }));
    }
}
