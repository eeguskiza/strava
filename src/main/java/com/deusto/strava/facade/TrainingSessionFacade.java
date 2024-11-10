package com.deusto.strava.facade;

import com.deusto.strava.entity.TrainingSession;
import com.deusto.strava.service.TrainingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody TrainingSession session) {
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
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
