package com.deusto.strava.service;

import com.deusto.strava.dto.UserDTO;
import com.deusto.strava.entity.User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private Map<String, User> registeredUsers = new HashMap<>();
    private Map<String, String> activeTokens = new HashMap<>();

    public String register(UserDTO userDTO) {
        if (registeredUsers.containsKey(userDTO.getEmail())) {
            return "User already registered.";
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setBirthDate(userDTO.getBirthDate());
        user.setWeight(userDTO.getWeight());
        user.setHeight(userDTO.getHeight());
        user.setMaxHeartRate(userDTO.getMaxHeartRate());
        user.setRestHeartRate(userDTO.getRestHeartRate());
        user.setPasswordHash(hashPassword(userDTO.getPassword())); // Hashea la contraseña

        registeredUsers.put(user.getEmail(), user);
        return "User registered successfully.";
    }

    public String login(String email, String password) {
        User user = registeredUsers.get(email);
        if (user == null) {
            return "User not found.";
        }

        if (!user.getPasswordHash().equals(hashPassword(password))) {
            return "Invalid password.";
        }

        String token = generateToken();
        activeTokens.put(token, email);
        return token;
    }

    public String logout(String token) {
        if (activeTokens.remove(token) != null) {
            return "User logged out successfully.";
        }
        return "Invalid token.";
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private String generateToken() {
        return java.util.UUID.randomUUID().toString();
    }
}
