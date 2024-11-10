package com.deusto.strava.service;

import com.deusto.strava.dto.ChallengeDTO;
import com.deusto.strava.dto.TrainingSessionDTO;
import com.deusto.strava.dto.UserDTO;
import com.deusto.strava.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private Map<String, User> registeredUsers = new HashMap<>();
    private Map<String, String> activeTokens = new HashMap<>();
    private Map<String, List<TrainingSessionDTO>> userTrainingSessions = new HashMap<>();
    private Map<String, List<ChallengeDTO>> userChallenges = new HashMap<>();
    private Map<String, List<ChallengeDTO>> acceptedChallenges = new HashMap<>();


    @PostConstruct
    public void init() {
        // Create a default user
        User defaultUser = new User();
        defaultUser.setEmail("tests");
        defaultUser.setName("Default User");

        // Parse the birth date from String to Date
        try {
            Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
            defaultUser.setBirthDate(birthDate);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle exception, or log it
        }

        defaultUser.setWeight(70.0F);
        defaultUser.setHeight(175.0F);
        defaultUser.setMaxHeartRate(190);
        defaultUser.setRestHeartRate(60);

        // Register the default user
        registeredUsers.put(defaultUser.getEmail(), defaultUser);

        // Optionally, log in the default user and generate a token
        String defaultToken = generateToken();
        activeTokens.put(defaultToken, defaultUser.getEmail());

        System.out.println("Default user registered with email: " + defaultUser.getEmail());
        System.out.println("Default token: " + defaultToken);
    }

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

    public String setUpChallenge(String token, ChallengeDTO challengeDTO) {
        String email = activeTokens.get(token);
        if (email == null) {
            return "Invalid token or user not logged in.";
        }

        userChallenges.putIfAbsent(email, new ArrayList<>());
        userChallenges.get(email).add(challengeDTO);

        System.out.println("Challenges for user " + email + ": " + userChallenges.get(email));
        return "Challenge set up successfully.";
    }


    public List<ChallengeDTO> downloadActiveChallenges(String token, String sport, String startDate, String endDate) {
        // Verifica que el token esté activo y obtiene el email del usuario
        String email = activeTokens.get(token);
        if (email == null) {
            throw new IllegalArgumentException("Invalid token or user not logged in.");
        }

        // Obtiene la lista de desafíos del usuario
        List<ChallengeDTO> challenges = userChallenges.getOrDefault(email, new ArrayList<>());

        // Filtra los desafíos activos (aquellos que no han finalizado)
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return challenges.stream()
                .filter(challenge -> {
                    try {
                        Date challengeEndDate = dateFormat.parse(challenge.getEndDate());
                        return challengeEndDate.after(currentDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .filter(challenge -> sport == null || challenge.getSport().equalsIgnoreCase(sport))
                .filter(challenge -> {
                    try {
                        if (startDate != null && endDate != null) {
                            Date challengeStartDate = dateFormat.parse(challenge.getStartDate());
                            Date filterStartDate = dateFormat.parse(startDate);
                            Date filterEndDate = dateFormat.parse(endDate);
                            return !challengeStartDate.before(filterStartDate) && !challengeStartDate.after(filterEndDate);
                        }
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public String acceptChallenge(String token, String challengeName) {
        String email = activeTokens.get(token);
        if (email == null) {
            return "Invalid token or user not logged in.";
        }

        // Obtener los desafíos del usuario
        List<ChallengeDTO> userChallengesList = userChallenges.getOrDefault(email, new ArrayList<>());

        // Busca el desafío en la lista de desafíos del usuario
        ChallengeDTO challengeToAccept = userChallengesList.stream()
                .filter(challenge -> challenge.getName().trim().equalsIgnoreCase(challengeName.trim()))
                .findFirst()
                .orElse(null);

        if (challengeToAccept == null) {
            System.out.println("Challenge '" + challengeName + "' not found in user's challenges.");
            return "Challenge not found.";
        }

        // Añadir el desafío a los desafíos aceptados
        acceptedChallenges.putIfAbsent(email, new ArrayList<>());
        acceptedChallenges.get(email).add(challengeToAccept);

        return "Challenge accepted successfully.";
    }


    public List<ChallengeDTO> getAcceptedChallenges(String token) {
        // Check if the token is active and retrieve the associated email
        String email = activeTokens.get(token);
        if (email == null) {
            throw new IllegalArgumentException("Invalid token or user not logged in.");
        }

        // Retrieve the list of accepted challenges or an empty list if none
        return acceptedChallenges.getOrDefault(email, new ArrayList<>());
    }

    public List<Map<String, Object>> getChallengeProgress(String token) {
        // Verifica que el token esté activo y obtiene el email del usuario
        String email = activeTokens.get(token);
        if (email == null) {
            throw new IllegalArgumentException("Invalid token or user not logged in.");
        }

        // Lista para almacenar el progreso de cada desafío
        List<Map<String, Object>> progressList = new ArrayList<>();

        // Recupera los desafíos aceptados por el usuario
        List<ChallengeDTO> challenges = acceptedChallenges.getOrDefault(email, new ArrayList<>());

        // Recupera las sesiones de entrenamiento del usuario
        List<TrainingSessionDTO> sessions = userTrainingSessions.getOrDefault(email, new ArrayList<>());

        // Formato de fecha para comparar las fechas de los desafíos y sesiones
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Calcula el progreso de cada desafío aceptado
        for (ChallengeDTO challenge : challenges) {
            double totalDistance = 0;
            double totalTime = 0;

            for (TrainingSessionDTO session : sessions) {
                try {
                    Date sessionDate = dateFormat.parse(session.getStartDate());
                    Date challengeStartDate = dateFormat.parse(challenge.getStartDate());
                    Date challengeEndDate = dateFormat.parse(challenge.getEndDate());

                    // Solo acumula las sesiones que están dentro del rango del desafío y del mismo deporte
                    if (!sessionDate.before(challengeStartDate) && !sessionDate.after(challengeEndDate)
                            && session.getSport().equalsIgnoreCase(challenge.getSport())) {

                        totalDistance += session.getDistance();
                        totalTime += session.getDuration();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Calcula el porcentaje de logro para distancia o tiempo
            double progress = 0;
            if (challenge.getTargetDistance() > 0) {
                progress = (totalDistance / challenge.getTargetDistance()) * 100;
            } else if (challenge.getTargetTime() > 0) {
                progress = (totalTime / challenge.getTargetTime()) * 100;
            }

            // Crea un mapa con el progreso del desafío
            Map<String, Object> challengeProgress = new HashMap<>();
            challengeProgress.put("challengeName", challenge.getName());
            challengeProgress.put("progress", Math.min(progress, 100)); // Máximo 100%
            challengeProgress.put("targetDistance", challenge.getTargetDistance());
            challengeProgress.put("targetTime", challenge.getTargetTime());
            challengeProgress.put("achievedDistance", totalDistance);
            challengeProgress.put("achievedTime", totalTime);

            progressList.add(challengeProgress);
        }

        return progressList;
    }



}
