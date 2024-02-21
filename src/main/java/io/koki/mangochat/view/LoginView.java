package io.koki.mangochat.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private final JButton loginButton;
    private final JTextField usernameTextField;
    private final JPasswordField passwordTextField;

    public LoginView() {
        GridBagConstraints constraints;
        Font font = new Font("consolas", Font.BOLD | Font.ITALIC, 16);
        JLabel label;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setTitle("login");
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());

        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.fill = GridBagConstraints.BOTH;

        label = new JLabel("username:");
        label.setFont(font);
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(label, constraints);

        label = new JLabel("password:");
        label.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(label, constraints);

        usernameTextField = new JTextField();
        usernameTextField.setPreferredSize(new Dimension(100, 0));
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(usernameTextField, constraints);

        passwordTextField = new JPasswordField();
        passwordTextField.setPreferredSize(new Dimension(100, 0));
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(passwordTextField, constraints);

        loginButton = new JButton("login");
        loginButton.setFont(font);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        add(loginButton, constraints);
    }

    public String getUsername() {
        return usernameTextField.getText();
    }

    public char[] getPassword() {
        return passwordTextField.getPassword();
    }

    public void setLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void displayMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public void clearFields() {
        usernameTextField.setText("");
        passwordTextField.setText("");
    }
}
