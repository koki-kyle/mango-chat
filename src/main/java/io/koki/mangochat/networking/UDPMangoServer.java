package io.koki.mangochat.networking;

import io.koki.mangochat.model.User;

import java.util.function.Predicate;

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

    @Override
    public void setUserAuthenticationPredicate(Predicate<User> predicate) {
        throw new RuntimeException("not implemented");
    }
}
