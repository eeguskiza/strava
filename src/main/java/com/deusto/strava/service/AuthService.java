package com.deusto.strava.service;

import com.deusto.strava.dto.TrainingSessionDTO;
import com.deusto.strava.dto.UserDTO;
import com.deusto.strava.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {
    // Stores registered users with their email as the key
    private Map<String, User> registeredUsers = new HashMap<>();

    // Maps active tokens to their associated user's email
    private Map<String, String> activeTokens = new HashMap<>();

    // Stores user training sessions, with the email as the key
    private Map<String, List<TrainingSessionDTO>> userTrainingSessions = new HashMap<>();

    // Registers a new user
    public String register(UserDTO userDTO) {
        // Check if the user is already registered
        if (registeredUsers.containsKey(userDTO.getEmail())) {
            return "User already registered.";
        }

        // Create a new User object and set its properties
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setBirthDate(userDTO.getBirthDate());
        user.setWeight(userDTO.getWeight());
        user.setHeight(userDTO.getHeight());
        user.setMaxHeartRate(userDTO.getMaxHeartRate());
        user.setRestHeartRate(userDTO.getRestHeartRate());

        // Add the user to the registered users map
        registeredUsers.put(user.getEmail(), user);
        return "User registered successfully.";
    }

    // Logs in a user by their email, generating a token if successful
    public String login(String email) {
        // Check if the user is registered
        User user = registeredUsers.get(email);
        if (user == null) {
            return "User not found.";
        }

        // Generate a token and associate it with the user's email
        String token = generateToken();
        activeTokens.put(token, email);
        return token;
    }

    // Logs out a user by removing their token from active tokens
    public boolean logout(String token) {
        return activeTokens.remove(token) != null;
    }

    // Generates a unique token for session management
    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    // Creates a new training session for the authenticated user
    public String createTrainingSession(String token, TrainingSessionDTO sessionDTO) {
        // Check if the token is active and retrieve the associated email
        String email = activeTokens.get(token);
        if (email == null) {
            return "Invalid token or user not logged in.";
        }

        // Add the training session to the user's session list
        userTrainingSessions.putIfAbsent(email, new ArrayList<>());
        userTrainingSessions.get(email).add(sessionDTO);

        return "Training session created successfully.";
    }

    // Queries the training sessions of the authenticated user, with optional date filtering
    public List<TrainingSessionDTO> queryTrainingSessions(String token, String startDate, String endDate) {
        // Check if the token is active and retrieve the associated email
        String email = activeTokens.get(token);
        if (email == null) {
            throw new IllegalArgumentException("Invalid token or user not logged in.");
        }

        // Retrieve the user's training sessions
        List<TrainingSessionDTO> sessions = userTrainingSessions.getOrDefault(email, new ArrayList<>());

        // Filter sessions by date range if startDate and endDate are provided
        if (startDate != null && endDate != null) {
            return sessions.stream()
                    .filter(session -> session.getStartDate().compareTo(startDate) >= 0 && session.getStartDate().compareTo(endDate) <= 0)
                    .collect(Collectors.toList());
        }

        // Return all sessions if no date range is specified
        return sessions;
    }
}
