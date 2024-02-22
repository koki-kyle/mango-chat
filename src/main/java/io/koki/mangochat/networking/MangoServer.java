package io.koki.mangochat.networking;

import io.koki.mangochat.model.User;

import java.util.function.Predicate;

public interface MangoServer {
    void startServer(int port);

    void stopServer();

    boolean isRunning();

    void setUserAuthenticationPredicate(Predicate<User> predicate);

    void setUserRegistrationPredicate(Predicate<User> predicate);
}
