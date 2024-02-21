package io.koki.mangochat.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DatabaseManager {
    private final List<User> users = new ArrayList<>();
    private final List<Message> messages = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public Optional<User> getUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> getUserByUsernameAndPassword(String username, char[] password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username)
                        && Arrays.equals(u.getPassword(), password))
                .findFirst();
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getAllMessages() {
        return new ArrayList<>(messages);
    }

    public void clearData() {
        users.clear();
        messages.clear();
    }

    public void demoData() {
        users.add(new User("koki", "jjna".toCharArray()));
        users.add(new User("thomas", "abacaxi".toCharArray()));
        users.add(new User("pablo", "españa".toCharArray()));
        users.add(new User("ivan", "javascript".toCharArray()));
    }
}
