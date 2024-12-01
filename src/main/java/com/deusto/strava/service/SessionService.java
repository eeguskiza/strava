package com.deusto.strava.service;

import com.deusto.strava.entity.TrainingSession;
import com.deusto.strava.entity.User;
import com.deusto.strava.dto.TrainingSessionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Autowired
    private AuthService authService;

    public String createSession(String token, TrainingSessionDTO sessionDTO) {

        Optional<User> userOptional = Optional.ofNullable(authService.getUserByToken(token));
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        User user = userOptional.get();


        TrainingSession session = new TrainingSession(
                sessionDTO.getSport(),
                sessionDTO.getDistance(),
                sessionDTO.getStartDate(),
                sessionDTO.getDuration()
        );

        user.getTrainingSessions().add(session);

        return "Training session created successfully with ID: " + session.getId();
    }

    public List<TrainingSession> getSessions(String token, Date startDate, Date endDate) {
        Optional<User> userOptional = Optional.ofNullable(authService.getUserByToken(token));
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        User user = userOptional.get();

        if (startDate != null && endDate != null) {
            return user.getTrainingSessions().stream()
                    .filter(session -> !session.getStartDate().before(startDate) &&
                            !session.getStartDate().after(endDate))
                    .collect(Collectors.toList());
        }

        return user.getTrainingSessions();
    }
}
