package com.deusto.strava.service;

import com.deusto.strava.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    // Simulated user repository
    private final Map<String, User> userRepository = new HashMap<>();

    // Simulated token store
    private final Map<String, String> tokenStore = new HashMap<>();

    // Register a new user
    public boolean register(User user) {
        if (user != null && user.getEmail() != null && !userRepository.containsKey(user.getEmail())) {
            userRepository.put(user.getEmail(), user);
            return true;
        }
        return false;
    }

    // Login a user and generate a token
    public String login(String email) {
        User user = userRepository.get(email);
        if (user != null) {
            String token = generateToken();
            tokenStore.put(token, email); // Associate the token with the user's email
            return token;
        }
        return null;
    }

    // Logout a user by removing the token
    public boolean logout(String token) {
        if (tokenStore.containsKey(token)) {
            tokenStore.remove(token);
            return true;
        }
        return false;
    }

    // Private helper method to generate a unique token
    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    // Method to retrieve a user by token (if needed in the future)
    public Optional<User> getUserByToken(String token) {
        String email = tokenStore.get(token);
        if (email != null) {
            return Optional.ofNullable(userRepository.get(email));
        }
        return Optional.empty();
    }
}
