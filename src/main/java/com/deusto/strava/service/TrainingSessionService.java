package com.deusto.strava.service;

import com.deusto.strava.dto.TrainingSessionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrainingSessionService {

    @Autowired
    private AuthService authService; // Inyectamos AuthService

    private Map<String, List<TrainingSessionDTO>> userTrainingSessions = new HashMap<>();

    // Creates a new training session for the authenticated user
    public String createTrainingSession(String token, TrainingSessionDTO sessionDTO) {
        // Verifica si el token es válido y obtiene el email asociado
        String email = authService.getEmailByToken(token); // Usamos authService para obtener el email por token
        if (email == null) {
            return "Invalid token or user not logged in.";
        }

        // Añade la sesión de entrenamiento a la lista del usuario
        userTrainingSessions.putIfAbsent(email, new ArrayList<>());
        userTrainingSessions.get(email).add(sessionDTO);

        return "Training session created successfully.";
    }

    // Queries the training sessions of the authenticated user, with optional date filtering
    public List<TrainingSessionDTO> queryTrainingSessions(String token, String startDate, String endDate) {
        // Verifica si el token es válido y obtiene el email asociado
        String email = authService.getEmailByToken(token); // Usamos authService para obtener el email por token
        if (email == null) {
            throw new IllegalArgumentException("Invalid token or user not logged in.");
        }

        // Recupera las sesiones de entrenamiento del usuario
        List<TrainingSessionDTO> sessions = userTrainingSessions.getOrDefault(email, new ArrayList<>());
        // Aquí puedes agregar la lógica para filtrar por fechas si se proporciona
        return sessions;
    }
}
