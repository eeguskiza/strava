package com.deusto.strava.service;

import com.deusto.strava.entity.Challenge;
import com.deusto.strava.entity.TrainingSession;
import com.deusto.strava.entity.User;
import com.deusto.strava.repository.IChallengeRepository;
import com.deusto.strava.repository.IUserRepository;
import com.deusto.strava.dto.ChallengeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengeService {

    @Autowired
    private AuthService authService; // To validate user tokens
    private final IChallengeRepository IChallengeRepository; // To access challenge data
    private final IUserRepository IUserRepository; // To access user data
    
	public ChallengeService(IChallengeRepository IChallengeRepository, IUserRepository IUserRepository) {
		this.IChallengeRepository = IChallengeRepository;
		this.IUserRepository = IUserRepository;
	}

    // Create a challenge and add it to the global list
    public String createChallenge(String token, ChallengeDTO challengeDTO) {
        try {
            // Log the incoming data
            System.out.println("Received token: " + token);
            System.out.println("Received ChallengeDTO: " + challengeDTO);

            // Verify the user
            Optional<User> userOptional = Optional.ofNullable(authService.getUserByToken(token));
            if (userOptional.isEmpty()) {
                System.out.println("Token invalid or expired.");
                throw new IllegalArgumentException("Invalid or expired token");
            }

            User user = userOptional.get();
            System.out.println("User found: " + user);

            // Map DTO to Entity
            Challenge challenge = new Challenge();
            challenge.setCreatorEmail(user.getEmail());
            challenge.setName(challengeDTO.getName());
            challenge.setSport(challengeDTO.getSport());
            challenge.setTargetDistance(challengeDTO.getTargetDistance());
            challenge.setTargetTime(challengeDTO.getTargetTime());
            challenge.setStartDate(challengeDTO.getStartDate());
            challenge.setEndDate(challengeDTO.getEndDate());
            System.out.println("Challenge entity created: " + challenge);

            // Save challenge to database and get ID
            Challenge savedChallenge = IChallengeRepository.save(challenge);
            System.out.println("Challenge saved to database with ID: " + savedChallenge.getId());

            // Add the saved challenge to the user's list
            user.getChallenges().add(savedChallenge);
            IUserRepository.save(user);
            System.out.println("User with updated challenges saved to database.");

            return "Challenge created successfully with ID: " + savedChallenge.getId();
        } catch (Exception e) {
            // Log any exception that occurs
            System.err.println("Error creating challenge: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }



    // Retrieve all active challenges
    public List<Challenge> getAllActiveChallenges() {
        // Get the current date
        Date currentDate = new Date(System.currentTimeMillis());
        List<Challenge>challenges = IChallengeRepository.findByEndDateGreaterThanEqual(currentDate);
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
        Optional<Challenge> challengeOptional = IChallengeRepository.findById(challengeId);

        if (challengeOptional.isEmpty()) {
            throw new IllegalArgumentException("Challenge not found: " + challengeId);
        }

        Challenge challenge = challengeOptional.get();

        // Check if the user has already accepted the challenge
        if (user.getChallenges().contains(challenge)) {
            throw new IllegalArgumentException("You have already joined this challenge: " + challenge.getName());
        }

        // Add the challenge to the user's list
        user.getChallenges().add(challenge);
        IUserRepository.save(user);

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
