package io.koki.mangochat.view;

import io.koki.mangochat.networking.MangoClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginView extends JFrame {
    private JButton loginButton;
    private JTextField usernameTextField;
    private boolean changingView = false;
    private final MangoClient mangoClient;

    public LoginView(MangoClient mangoClient) {
        this.mangoClient = mangoClient;

        initComponents();
        setupUI();
    }

    private void initComponents() {
        usernameTextField = new JTextField();
        usernameTextField.setPreferredSize(new Dimension(100, 0));

        loginButton = new JButton("login");
        loginButton.addActionListener(e -> {
            String username = usernameTextField.getText();

            if (mangoClient.login(username)) {
                displayMessage(String.format("welcome %s", username), "login successful", JOptionPane.INFORMATION_MESSAGE);
                changeView(username);
            } else {
                displayMessage(String.format("%s is already taken", username), "error", JOptionPane.ERROR_MESSAGE);
                usernameTextField.setText("");
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setTitle("login");
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (changingView) {
                    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                } else {
                    mangoClient.disconnect();
                }
            }
        });

    }

    private void changeView(String username) {
        changingView = true;

        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

        SwingUtilities.invokeLater(() -> new ChatView(mangoClient, username).setVisible(true));
    }

    private void displayMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private void setupUI() {
        GridBagConstraints constraints;
        Font font = new Font("consolas", Font.BOLD | Font.ITALIC, 16);
        JLabel label;

        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.fill = GridBagConstraints.BOTH;

        label = new JLabel("username:");
        label.setFont(font);
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(label, constraints);

        usernameTextField.setFont(font);
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(usernameTextField, constraints);

        loginButton.setFont(font);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        add(loginButton, constraints);
    }
}
