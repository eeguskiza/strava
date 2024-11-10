package com.deusto.strava.service;

import com.deusto.strava.dto.UserDTO;
import com.deusto.strava.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private Map<String, User> registeredUsers = new HashMap<>();
    private Map<String, String> activeTokens = new HashMap<>();

    // Registers a new user
    public String register(User user) {
        if (registeredUsers.containsKey(user.getEmail())) {
            return "User already registered.";
        }
        registeredUsers.put(user.getEmail(), user);
        return "User registered successfully.";
    }

    // Logs in a user by their email, generating a token if successful
    public String login(String email) {
        User user = registeredUsers.get(email);
        if (user == null) {
            return "User not found.";
        }

        String token = generateToken();
        activeTokens.put(token, email);
        return token;
    }

    public boolean logout(String token) {
        return activeTokens.remove(token) != null;
    }

    // Generates a unique token for session management
    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public String getEmailByToken(String token) {
        return activeTokens.get(token);
    }

}
