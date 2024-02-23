package io.koki.mangochat.view;

import io.koki.mangochat.model.Message;
import io.koki.mangochat.networking.MangoClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ChatView extends JFrame {
    private JTextArea chatTextArea;
    private JTextField messageTextField;
    private JButton sendButton;
    private boolean changingView = false;
    private final MangoClient mangoClient;
    private String username;

    public ChatView(MangoClient mangoClient, String username) {
        this.mangoClient = mangoClient;
        this.username = username;

        initComponents();
        setupUI();

        loadChatHistory();
        new Thread(this::updateChat).start();
    }

    private void initComponents() {
        chatTextArea = new JTextArea();
        chatTextArea.setLineWrap(true);
        chatTextArea.setEditable(false);

        messageTextField = new JTextField();

        sendButton = new JButton("send");
        sendButton.addActionListener(e -> {
            String body = messageTextField.getText();
            Message message = new Message(username, body);

            if (body.charAt(0) == '/') {
                String command = body.substring(1);
                String[] param = command.split(" ");

                if (param.length == 1) {
                    if (param[0].equalsIgnoreCase("quit")) {
                        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                    } else if (param[0].equalsIgnoreCase("logout")) {
                        mangoClient.disconnect();

                        changingView = true;
                        changeView();
                    } else {
                        displayMessage("invalid command", "error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    displayMessage("invalid command", "error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                chatTextArea.append(message.getMessage());
                messageTextField.setText("");
            }

            mangoClient.sendMessage(message);
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setTitle("chat");
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

    private void loadChatHistory() {
        for (Message message : mangoClient.fetchChatHistory()) {
            chatTextArea.append(message.getMessage());
        }
    }

    private void updateChat() {
        while (true) {
            try {
                chatTextArea.append(mangoClient.fetchMessage().getMessage());
            } catch (IOException e) {
                mangoClient.disconnect();
            } catch (ClassNotFoundException ignore) {}
        }
    }

    private void changeView() {
        changingView = true;

        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

        SwingUtilities.invokeLater(() -> new LoginView(mangoClient).setVisible(true));
    }

    private void displayMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private void setupUI() {
        GridBagConstraints constraints = new GridBagConstraints();
        Font font = new Font("consolas", Font.BOLD | Font.ITALIC, 16);
        JScrollPane scrollPane;

        constraints.fill = GridBagConstraints.BOTH;

        chatTextArea.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 29;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(20, 20, 10, 20);
        scrollPane = new JScrollPane(chatTextArea);
        scrollPane.setPreferredSize(new Dimension(0, 0));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, constraints);

        messageTextField.setFont(font);
        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.weightx = 5;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(10, 20, 20, 10);
        add(messageTextField, constraints);

        constraints.gridy = 1;
        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.insets = new Insets(10, 10, 20, 20);
        add(sendButton, constraints);
    }
}
