package io.koki.mangochat.config;

public class AppConfig {
    private static CommunicationMode communicationMode;

    public static void setCommunicationMode(CommunicationMode mode) {
        communicationMode = mode;
    }

    public static CommunicationMode getCommunicationMode() {
        return communicationMode;
    }
}
