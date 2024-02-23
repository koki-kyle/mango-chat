package io.koki.mangochat;

import io.koki.mangochat.config.AppConfig;
import io.koki.mangochat.config.CommunicationMode;
import io.koki.mangochat.networking.MangoClient;
import io.koki.mangochat.networking.tcp.TCPMangoClient;
import io.koki.mangochat.view.LoginView;

import javax.swing.*;

public class MangoChatClient {
    public static void main(String[] args) {
        AppConfig.setCommunicationMode(CommunicationMode.TCP);

        MangoClient mangoClient = new TCPMangoClient();

        SwingUtilities.invokeLater(() -> new LoginView(mangoClient).setVisible(true));
    }
}
