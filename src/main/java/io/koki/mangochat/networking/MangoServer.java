package io.koki.mangochat.networking;

public interface MangoServer {
    void startServer(int port);

    void stopServer();
    boolean isRunning();
}
