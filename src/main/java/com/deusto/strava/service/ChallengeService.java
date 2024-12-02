package com.deusto.strava.service;

import com.deusto.strava.entity.Challenge;
import com.deusto.strava.entity.TrainingSession;
import com.deusto.strava.entity.User;
import com.deusto.strava.repository.ChallengeRepository;
import com.deusto.strava.repository.UserRepository;
import com.deusto.strava.dto.ChallengeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChallengeService {

    @Autowired
    private AuthService authService; // To validate user tokens
    private final ChallengeRepository challengeRepository; // To access challenge data
    private final UserRepository userRepository; // To access user data
    
	public ChallengeService(ChallengeRepository challengeRepository, UserRepository userRepository) {
		this.challengeRepository = challengeRepository;
		this.userRepository = userRepository;
	}

    // Create a challenge and add it to the global list
    public String createChallenge(String token, ChallengeDTO challengeDTO) {
        Optional<User> userOptional = Optional.ofNullable(authService.getUserByToken(token));
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        User user = userOptional.get();

        Challenge challenge = new Challenge();
        challenge.setId(Long.valueOf("challenge_" + (++Challenge.count)));
        challenge.setCreatorEmail(user.getEmail());
        challenge.setName(challengeDTO.getName());
        challenge.setSport(challengeDTO.getSport());
        challenge.setTargetDistance(challengeDTO.getTargetDistance());
        challenge.setTargetTime(challengeDTO.getTargetTime());
        challenge.setStartDate(challengeDTO.getStartDate());
        challenge.setEndDate(challengeDTO.getEndDate());

        // Add the challenge to the user's list
        user.getChallenges().add(challenge);

        // Add the challenge to the global list
        userRepository.save(user);

        return "Challenge created successfully with ID: " + challenge.getId();
    }

    // Retrieve all active challenges
    public List<Challenge> getAllActiveChallenges() {
        Date currentDate = new Date();
        List<Challenge>challenges = challengeRepository.findByEndDateGreaterThanEqual(currentDate);
        return challenges;
    }

    // Method to accept a challenge
    public String acceptChallenge(String token, Long challengeId) {
        // Validate token and get the user
        Optional<User> userOptional = Optional.ofNullable(authService.getUserByToken(token));
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        User user = userOptional.get();

        // Find the challenge by ID in the global list
        Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

        if (!challengeOptional.isPresent()) {
            throw new IllegalArgumentException("Challenge not found: " + challengeId);
        }

        Challenge challenge = challengeOptional.get();

        // Check if the user has already accepted the challenge
        if (user.getChallenges().contains(challenge)) {
            return "You have already accepted this challenge: " + challenge.getName();
        }

        // Add the challenge to the user's list
        user.getChallenges().add(challenge);
        userRepository.save(user);

        return "Challenge accepted successfully: " + challenge.getName();
    }

    // Method to retrieve challenges accepted by the authenticated user
    public List<Challenge> getMyChallenges(String token) {
        // Validate token and get the user
        User user = authService.getUserByToken(token);
        if (user == null) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        // Return the list of challenges accepted by the user
        return user.getChallenges();
    }

    public String getChallengeProgress(String token, Long challengeId) {
        // Obtener el usuario asociado al token
        User user = authService.getUserByToken(token);
        if (user == null) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        // Buscar el desafío en la lista de desafíos del usuario
        Challenge challenge = user.getChallenges().stream()
                .filter(c -> c.getId().equals(challengeId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found for this user"));

        // Calcular el progreso: sesiones relacionadas con el deporte del desafío
        float totalDistance = 0;
        float totalDuration = 0;

        for (TrainingSession session : user.getTrainingSessions()) {
            if (session.getSport().equalsIgnoreCase(challenge.getSport())
                    && !session.getStartDate().before(challenge.getStartDate())
                    && !session.getStartDate().after(challenge.getEndDate())) {
                totalDistance += session.getDistance();
                totalDuration += session.getDuration();
            }
        }

        // Calcular porcentajes de progreso
        float distanceProgress = (totalDistance / challenge.getTargetDistance()) * 100;
        float durationProgress = (totalDuration / challenge.getTargetTime()) * 100;

        // Formatear el progreso como una cadena
        return String.format("Progress: %.2f%% of distance, %.2f%% of time",
                Math.min(distanceProgress, 100), Math.min(durationProgress, 100));
    }

}
