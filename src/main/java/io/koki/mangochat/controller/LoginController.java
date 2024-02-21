package io.koki.mangochat.controller;

import io.koki.mangochat.model.User;
import io.koki.mangochat.networking.MangoClient;
import io.koki.mangochat.view.ChatView;
import io.koki.mangochat.view.LoginView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;

public class LoginController {
    private final MangoClient mangoClient;
    private final LoginView loginView;
    private boolean changeView = false;

    public LoginController(MangoClient mangoClient, LoginView loginView) {
        this.mangoClient = mangoClient;
        this.loginView = loginView;

        loginView.setLoginButtonListener(e -> {
            ChatController chatController;
            String username = loginView.getUsername();
            char[] password = loginView.getPassword();

            loginView.clearFields();

            if (!mangoClient.authenticate(new User(username, password))) {
                loginView.displayMessage("username or password incorrect", "error", JOptionPane.ERROR_MESSAGE);
            } else {
                loginView.displayMessage(String.format("welcome %s!!", username), "login successful", JOptionPane.INFORMATION_MESSAGE);
                changeView = true;

                loginView.dispatchEvent(new WindowEvent(loginView, WindowEvent.WINDOW_CLOSING));

                chatController = new ChatController(mangoClient, new ChatView());
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
                    mangoClient.disconnect();
                }
            }
        });
    }

    public void show() {
        SwingUtilities.invokeLater(() -> loginView.setVisible(true));
    }

    public void setUpServer(InetAddress address, int port) {
        mangoClient.setUpServer(address, port);
    }
}
