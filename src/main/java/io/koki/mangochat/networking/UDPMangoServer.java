package io.koki.mangochat.networking;

public class UDPMangoServer implements MangoServer {
    @Override
    public void startServer(int port) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void stopServer() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
