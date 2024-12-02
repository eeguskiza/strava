package com.deusto.strava.service;

import com.deusto.strava.entity.TrainingSession;
import com.deusto.strava.entity.User;
import com.deusto.strava.repository.TrainingSessionRepository;
import com.deusto.strava.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final TrainingSessionRepository trainingSessionRepository;

	public SessionService(UserRepository userRepository, TrainingSessionRepository trainingSessionRepository) {
		this.userRepository = userRepository;
		this.trainingSessionRepository = trainingSessionRepository;
	}

    public String createSession(String token, TrainingSessionDTO sessionDTO) {
        User user = authService.getUserByToken(token);
        if (user==null) {
            throw new IllegalArgumentException("Invalid or expired token");
        }


        TrainingSession session = new TrainingSession(
                sessionDTO.getSport(),
                sessionDTO.getDistance(),
                (java.sql.Date) sessionDTO.getStartDate(),
                sessionDTO.getDuration()
        );

        user.getTrainingSessions().add(session);
        userRepository.save(user);

        return "Training session created successfully with ID: " + session.getId();
    }

    public List<TrainingSession> getSessions(String token, Date startDate, Date endDate) {
        User user = authService.getUserByToken(token);
        if (user==null) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        if (startDate != null && endDate != null) {
            return user.getTrainingSessions().stream()
                    .filter(session -> !session.getStartDate().before(startDate) &&
                            !session.getStartDate().after(endDate))
                    .collect(Collectors.toList());
        }

        return user.getTrainingSessions();
    }
}
