package com.deusto.strava.facade;

import com.deusto.strava.dto.TrainingSessionDTO;
import com.deusto.strava.entity.TrainingSession;
import com.deusto.strava.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

	public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
	}
    /**
     * Endpoint to create a new training session for an authenticated user.
     * @param token The token passed in the request header for user authentication.
     * @param sessionDTO The training session data provided in the request body.
     * @return A response entity containing a success message or an error message.
     */
    @PostMapping
    public ResponseEntity<String> createSession(
            @RequestHeader("token") String token, // Token provided in the request header
            @RequestBody TrainingSessionDTO sessionDTO) { // Training session data in the request body
        try {
            String response = sessionService.createSession(token, sessionDTO);
            return ResponseEntity.ok(response); // Successful response
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("Error: " + e.getMessage()); // Invalid or expired token
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: An unexpected error occurred."); // General error
        }
    }

    /**
     * Endpoint to retrieve training sessions for an authenticated user.
     * Allows optional filtering by date range.
     * @param token The token passed in the request header for user authentication.
     * @param startDate (Optional) Start date for filtering sessions.
     * @param endDate (Optional) End date for filtering sessions.
     * @return A response entity containing a list of training sessions.
     */
    @GetMapping
    public ResponseEntity<List<TrainingSessionDTO>> getSessions(
            @RequestHeader("token") String token, // Token provided in the request header
            @RequestParam(required = false) Date startDate, // Optional start date for filtering
            @RequestParam(required = false) Date endDate // Optional end date for filtering
    ) {
        List<TrainingSession> sessions = sessionService.getSessions(token, startDate, endDate);
        List<TrainingSessionDTO>sessionsDTO = new ArrayList<>();
        for(TrainingSession session : sessions){
            sessionsDTO.add(sesssionToDTO(session));
        }
        return ResponseEntity.ok(sessionsDTO); // Successful response with the list of sessions
    }

	public TrainingSessionDTO sesssionToDTO(TrainingSession session){
        return new TrainingSessionDTO(session.getId(), session.getSport(), session.getDistance(), session.getStartDate(), session.getDuration());
    }
}
