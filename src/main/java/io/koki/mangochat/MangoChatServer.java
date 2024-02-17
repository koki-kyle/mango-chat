package io.koki.mangochat;

import io.koki.mangochat.config.AppConfig;
import io.koki.mangochat.config.CommunicationMode;
import io.koki.mangochat.controller.ServerController;
import io.koki.mangochat.model.DatabaseManager;
import io.koki.mangochat.networking.Server;
import io.koki.mangochat.networking.TCPServer;
import io.koki.mangochat.networking.UDPServer;

public class MangoChatServer {
    public static void main(String[] args) {
        // configure the communication mode, either TCP or UDP
        AppConfig.setCommunicationMode(CommunicationMode.TCP);

        // setup initial components
        Server server = AppConfig.getCommunicationMode() == CommunicationMode.TCP
                ? new TCPServer()
                : new UDPServer();
        DatabaseManager databaseManager = new DatabaseManager();
        ServerController serverController = new ServerController(server, databaseManager);

        // start the server
        int port = 1234;
        serverController.startServer(port);

        System.out.printf("server running on port %d%n", port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("shutting down server...");
            serverController.stopServer();
        }));
    }
}
