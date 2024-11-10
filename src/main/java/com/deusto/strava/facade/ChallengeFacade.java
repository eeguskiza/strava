package com.deusto.strava.facade;

import com.deusto.strava.entity.*;
import com.deusto.strava.service.ChallengeService;
import com.deusto.strava.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeFacade {

    private final AuthService authService;
    private final ChallengeService challengeService;

    // Usamos un solo constructor para inyectar ambas dependencias
    @Autowired
    public ChallengeFacade(AuthService authService, ChallengeService challengeService) {
        this.authService = authService;
        this.challengeService = challengeService;
    }

    // Set up challenge
    @PostMapping
    public ResponseEntity<String> setUpChallenge(
            @RequestHeader("token") String token,
            @RequestBody Challenge challenge) {
        String result = challengeService.setUpChallenge(token, challenge);  // Cambiado de authService a challengeService
        if ("Challenge set up successfully.".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
    }

    // Get active challenges
    @GetMapping("/active")
    public ResponseEntity<List<Challenge>> downloadActiveChallenges(
            @RequestHeader("token") String token,
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        List<Challenge> challenges = challengeService.downloadActiveChallenges(token, sport, startDate, endDate);  // Cambiado de authService a challengeService
        return new ResponseEntity<>(challenges, HttpStatus.OK);
    }

    // Accept challenge
    @PostMapping("/accept")
    public ResponseEntity<String> acceptChallenge(
            @RequestHeader("token") String token,
            @RequestBody Map<String, String> body) {
        String challengeName = body.get("challengeName");
        String result = challengeService.acceptChallenge(token, challengeName);  // Cambiado de authService a challengeService

        if ("Challenge accepted successfully.".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else if (result.equals("Challenge not found.") || result.equals("No challenges found for this user.")) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
    }

    // Get accepted challenges
    @GetMapping("/accepted")
    public ResponseEntity<List<Challenge>> getAcceptedChallenges(
            @RequestHeader("token") String token) {
        List<Challenge> acceptedChallenges = challengeService.getAcceptedChallenges(token);  // Cambiado de authService a challengeService
        return new ResponseEntity<>(acceptedChallenges, HttpStatus.OK);
    }

    // Get challenge progress
    @GetMapping("/progress")
    public ResponseEntity<?> getChallengeProgress(@RequestHeader("token") String token) {
        try {
            List<Map<String, Object>> progress = challengeService.getChallengeProgress(token);  // Cambiado de authService a challengeService
            return new ResponseEntity<>(progress, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
