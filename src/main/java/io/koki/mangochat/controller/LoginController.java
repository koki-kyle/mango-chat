package io.koki.mangochat.controller;

import io.koki.mangochat.model.User;
import io.koki.mangochat.networking.Client;
import io.koki.mangochat.view.ChatView;
import io.koki.mangochat.view.LoginView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;

public class LoginController {
    private final Client client;
    private final LoginView loginView;
    private boolean changeView = false;

    public LoginController(Client client, LoginView loginView) {
        this.client = client;
        this.loginView = loginView;

        loginView.setLoginButtonListener(e -> {
            ChatController chatController;
            String username = loginView.getUsername();
            char[] password = loginView.getPassword();

            loginView.clearFields();

            if (!client.authenticate(new User(username, password))) {
                loginView.displayErrorMessage("username or password incorrect");
            } else {
                loginView.displayErrorMessage(String.format("welcome %s!!", username));
                changeView = true;
                loginView.dispatchEvent(new WindowEvent(loginView, WindowEvent.WINDOW_CLOSING));
                chatController = new ChatController(client, new ChatView());
                chatController.show();
            }
        });

        loginView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (changeView) {
                    System.out.println("changing view");
                    loginView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                } else {
                    client.disconnect();
                }
            }
        });
    }

    public void show() {
        SwingUtilities.invokeLater(() -> loginView.setVisible(true));
    }

    public void setUpServer(InetAddress address, int port) {
        client.setUpServer(address, port);
    }
}
