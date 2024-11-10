package com.deusto.strava.service;

import com.deusto.strava.dto.TrainingSessionDTO;
import com.deusto.strava.dto.UserDTO;
import com.deusto.strava.entity.User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public boolean logout(String token) {
        return activeTokens.remove(token) != null;
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

    private Map<String, List<TrainingSessionDTO>> userTrainingSessions = new HashMap<>();

    public String createTrainingSession(String token, TrainingSessionDTO sessionDTO) {
        // Verifica que el token esté activo
        String email = activeTokens.get(token);
        if (email == null) {
            return "Invalid token or user not logged in.";
        }

        // Añade la sesión al usuario
        userTrainingSessions.putIfAbsent(email, new ArrayList<>());
        userTrainingSessions.get(email).add(sessionDTO);

        return "Training session created successfully.";
    }

    public List<TrainingSessionDTO> queryTrainingSessions(String token, String startDate, String endDate) {
        // Verifica que el token esté activo y obtiene el email asociado
        String email = activeTokens.get(token);
        if (email == null) {
            throw new IllegalArgumentException("Invalid token or user not logged in.");
        }

        // Obtiene la lista de sesiones de entrenamiento del usuario
        List<TrainingSessionDTO> sessions = userTrainingSessions.getOrDefault(email, new ArrayList<>());

        // Filtra las sesiones por fecha, si se proporciona startDate y endDate
        if (startDate != null && endDate != null) {
            return sessions.stream()
                    .filter(session -> session.getStartDate().compareTo(startDate) >= 0 && session.getStartDate().compareTo(endDate) <= 0)
                    .collect(Collectors.toList());
        }

        return sessions; // Devuelve todas las sesiones si no hay filtros de fecha
    }
}
