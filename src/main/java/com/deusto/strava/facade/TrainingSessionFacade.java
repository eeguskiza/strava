package com.deusto.strava.facade;

import com.deusto.strava.dto.TrainingSessionDTO;
import com.deusto.strava.entity.TrainingSession;
import com.deusto.strava.service.TrainingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class TrainingSessionFacade {

    private final TrainingSessionService trainingSessionService;

    @Autowired
    public TrainingSessionFacade(TrainingSessionService trainingSessionService) {
        this.trainingSessionService = trainingSessionService;
    }

    // Endpoint to create a training session
    @PostMapping
    public ResponseEntity<String> createTrainingSession(
            @RequestHeader("token") String token,
            @RequestBody TrainingSessionDTO sessiondto) {
    	TrainingSession session = new TrainingSession(sessiondto.getTitle(), sessiondto.getSport(), sessiondto.getDistance(), sessiondto.getStartDate(), sessiondto.getStartTime(), sessiondto.getDuration());
        String result = trainingSessionService.createTrainingSession(token, session);

        if ("Training session created successfully.".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
    }

    // Endpoint to query training sessions
    @GetMapping
    public ResponseEntity<?> queryTrainingSessions(
            @RequestHeader("token") String token,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            List<TrainingSession> sessions = trainingSessionService.queryTrainingSessions(token, startDate, endDate);
            List<TrainingSessionDTO> sessionDTOs = new ArrayList<>();
			for (TrainingSession session : sessions) {
				sessionDTOs.add(TrainingSessiontoDTO(session));
			}
            return new ResponseEntity<>(sessionDTOs, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

	private TrainingSessionDTO TrainingSessiontoDTO(TrainingSession session) {
		return new TrainingSessionDTO(session.getTitle(), session.getSport(), session.getDistance(),
				session.getStartDate(), session.getStartTime(), session.getDuration());
	}
}
