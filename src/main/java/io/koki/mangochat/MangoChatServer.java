package io.koki.mangochat;

import io.koki.mangochat.config.AppConfig;
import io.koki.mangochat.config.CommunicationMode;

public class MangoChatServer {
    public static void main(String[] args) {
        AppConfig.setCommunicationMode(CommunicationMode.TCP);
    }
}
