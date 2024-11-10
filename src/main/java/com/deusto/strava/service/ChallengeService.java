package com.deusto.strava.service;

import com.deusto.strava.entity.*;
import com.deusto.strava.dto.TrainingSessionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ChallengeService {

    @Autowired
    private AuthService authService; // Inyectamos AuthService

    private Map<String, List<Challenge>> userChallenges = new HashMap<>();
    private Map<String, List<Challenge>> acceptedChallenges = new HashMap<>();

    // Set up a new challenge
    public String setUpChallenge(String token, Challenge challenge) {
        String email = authService.getEmailByToken(token); // Usamos AuthService para obtener el email por token
        if (email == null) {
            return "Invalid token or user not logged in.";
        }

        userChallenges.putIfAbsent(email, new ArrayList<>());
        userChallenges.get(email).add(challenge);
        return "Challenge set up successfully.";
    }

    // Download active challenges for the user
    public List<Challenge> downloadActiveChallenges(String token, String sport, String startDate, String endDate) {
        String email = authService.getEmailByToken(token); // Usamos AuthService para obtener el email por token
        if (email == null) {
            throw new IllegalArgumentException("Invalid token or user not logged in.");
        }

        List<Challenge> challenges = userChallenges.getOrDefault(email, new ArrayList<>());
        // Aquí puedes agregar el filtro por fechas y deporte si se proporciona
        return challenges;
    }

    // Accept a challenge
    public String acceptChallenge(String token, String challengeName) {
        String email = authService.getEmailByToken(token); // Usamos AuthService para obtener el email por token
        if (email == null) {
            return "Invalid token or user not logged in.";
        }

        List<Challenge> userChallengesList = userChallenges.getOrDefault(email, new ArrayList<>());
        Challenge challengeToAccept = userChallengesList.stream()
                .filter(challenge -> challenge.getName().equalsIgnoreCase(challengeName))
                .findFirst()
                .orElse(null);

        if (challengeToAccept == null) {
            return "Challenge not found.";
        }

        acceptedChallenges.putIfAbsent(email, new ArrayList<>());
        acceptedChallenges.get(email).add(challengeToAccept);

        return "Challenge accepted successfully.";
    }

    // Get accepted challenges for the user
    public List<Challenge> getAcceptedChallenges(String token) {
        String email = authService.getEmailByToken(token); // Usamos AuthService para obtener el email por token
        if (email == null) {
            throw new IllegalArgumentException("Invalid token or user not logged in.");
        }

        return acceptedChallenges.getOrDefault(email, new ArrayList<>());
    }

    // Get challenge progress
    public Map<Challenge, Double> getChallengeProgress(String token) {
        String email = authService.getEmailByToken(token); // Usamos AuthService para obtener el email por token
        if (email == null) {
            throw new IllegalArgumentException("Invalid token or user not logged in.");
        }

        List<Challenge> challenges = acceptedChallenges.getOrDefault(email, new ArrayList<>());
        List<TrainingSession> sessions = TrainingSessionService.userTrainingSessions.getOrDefault(email, new ArrayList<>());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<Challenge, Double> challengeProgress = new HashMap<>();
        for (Challenge challenge : challenges) {
            double totalDistance = 0;
            double totalTime = 0;

            for (TrainingSession session : sessions) {
                try {
                    Date sessionDate = dateFormat.parse(session.getStartDate());
                    Date challengeStartDate = dateFormat.parse(challenge.getStartDate());
                    Date challengeEndDate = dateFormat.parse(challenge.getEndDate());

                    if (!sessionDate.before(challengeStartDate) && !sessionDate.after(challengeEndDate)
                            && session.getSport().equalsIgnoreCase(challenge.getSport())) {

                        totalDistance += session.getDistance();
                        totalTime += session.getDuration();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            double progress = 0;
            if (challenge.getTargetDistance() > 0) {
                progress = (totalDistance / challenge.getTargetDistance()) * 100;
            } else if (challenge.getTargetTime() > 0) {
                progress = (totalTime / challenge.getTargetTime()) * 100;
            }

            
            challengeProgress.put(challenge, Math.min(progress, 100));
        }

        return challengeProgress;
    }
}
