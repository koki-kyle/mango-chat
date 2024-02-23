package io.koki.mangochat;

import io.koki.mangochat.config.AppConfig;
import io.koki.mangochat.config.CommunicationMode;
import io.koki.mangochat.networking.MangoServer;
import io.koki.mangochat.networking.tcp.TCPMangoServer;

public class MangoChatServer {
    public static void main(String[] args) {
        // configure the communication mode, either TCP or UDP
        AppConfig.setCommunicationMode(CommunicationMode.TCP);

        // setup initial components
        MangoServer mangoServer = new TCPMangoServer();

        // start the server
        mangoServer.startServer(1234);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("shutting down server...");
            mangoServer.stopServer();
        }));
    }
}
