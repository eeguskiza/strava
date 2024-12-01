package com.deusto.strava.facade;

import com.deusto.strava.dto.ChallengeDTO;
import com.deusto.strava.entity.Challenge;
import com.deusto.strava.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    /**
     * Endpoint to create a new challenge.
     * @param token The token passed in the request header for user authentication.
     * @param challengeDTO The challenge data provided in the request body.
     * @return A response entity containing a success message or an error message.
     */
    @PostMapping
    public ResponseEntity<String> createChallenge(
            @RequestHeader("token") String token, // Token provided in the request header
            @RequestBody ChallengeDTO challengeDTO) { // Challenge data in the request body
        try {
            String response = challengeService.createChallenge(token, challengeDTO);
            return ResponseEntity.ok(response); // Successful response
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("Error: " + e.getMessage()); // Invalid or expired token
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: An unexpected error occurred."); // General error
        }
    }

    /**
     * Endpoint to retrieve all active challenges.
     * @return A response entity containing the list of all active challenges.
     */
    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveChallenges() {
        try {
            List<Challenge> activeChallenges = challengeService.getAllActiveChallenges();
            return ResponseEntity.ok(activeChallenges); // Successful response
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: An unexpected error occurred."); // General error
        }
    }

    /**
     * Endpoint to accept a challenge.
     * @param token The token passed in the request header for user authentication.
     * @param challengeId The ID of the challenge to accept.
     * @return A response entity containing a success message or an error message.
     */
    @PostMapping("/enrollment")
    public ResponseEntity<String> acceptChallenge(
            @RequestHeader("token") String token, // Token provided in the request header
            @RequestParam("challengeId") String challengeId) { // Challenge ID provided as a request parameter
        try {
            String response = challengeService.acceptChallenge(token, challengeId);
            return ResponseEntity.ok(response); // Successful response
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage()); // Challenge not found or invalid token
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: An unexpected error occurred."); // General error
        }
    }

    /**
     * Endpoint to retrieve challenges accepted by the authenticated user.
     * @param token The token passed in the request header for user authentication.
     * @return A response entity containing the list of challenges or an error message.
     */
    @GetMapping("/my-active")
    public ResponseEntity<?> getMyChallenges(@RequestHeader("token") String token) {
        try {
            List<Challenge> myChallenges = challengeService.getMyChallenges(token);
            return ResponseEntity.ok(myChallenges); // Successful response
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("Error: " + e.getMessage()); // Invalid or expired token
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: An unexpected error occurred."); // General error
        }
    }

    @GetMapping("/progress")
    public ResponseEntity<String> getChallengeProgress(
            @RequestHeader("token") String token, // Token del usuario en los headers
            @RequestParam("challengeId") String challengeId // ID del desafío en el query parameter
    ) {
        try {
            String progress = challengeService.getChallengeProgress(token, challengeId);
            return ResponseEntity.ok(progress); // Respuesta con el progreso
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage()); // Error de parámetros inválidos
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: An unexpected error occurred."); // Error general
        }
    }



}

