package com.deusto.strava.facade;

import com.deusto.strava.dto.ChallengeDTO;
import com.deusto.strava.entity.*;
import com.deusto.strava.service.ChallengeService;
import com.deusto.strava.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
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
            @RequestBody ChallengeDTO challengedto) {
    	Challenge challenge = new Challenge(challengedto.getName(), challengedto.getSport(), challengedto.getTargetDistance(), challengedto.getTargetTime(), challengedto.getStartDate(), challengedto.getEndDate());
        String result = challengeService.setUpChallenge(token, challenge);  // Cambiado de authService a challengeService
        if ("Challenge set up successfully.".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
    }

    // Get active challenges
    @GetMapping("/active")
    public ResponseEntity<List<ChallengeDTO>> downloadActiveChallenges(
            @RequestHeader("token") String token,
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        List<Challenge> challenges = challengeService.downloadActiveChallenges(token, sport, startDate, endDate);  // Cambiado de authService a challengeService
        List<ChallengeDTO> challengesdtos = new ArrayList<>();
		for (Challenge challenge : challenges) {
			challengesdtos.add(new ChallengeDTO(challenge.getName(), challenge.getSport(), challenge.getTargetDistance(),
							challenge.getTargetTime(), challenge.getStartDate(), challenge.getEndDate()));
		}
        return new ResponseEntity<>(challengesdtos, HttpStatus.OK);
    }

    // Accept challenge
    @PostMapping("/accept")
    public ResponseEntity<String> acceptChallenge(
            @RequestHeader("token") String token,
            @RequestBody ChallengeDTO challengeDTO) {
        String challengeName = challengeDTO.getName();
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
    public ResponseEntity<List<ChallengeDTO>> getAcceptedChallenges(
            @RequestHeader("token") String token) {
        List<Challenge> acceptedChallenges = challengeService.getAcceptedChallenges(token);  // Cambiado de authService a challengeService
        List<ChallengeDTO> acceptedChallengesDTOs = new ArrayList<>();
                for (Challenge challenge : acceptedChallenges) {
					acceptedChallengesDTOs.add(challengeToDTO(challenge));
                }
        return new ResponseEntity<>(acceptedChallengesDTOs, HttpStatus.OK);
    }

    // Get challenge progress
    @GetMapping("/progress")
    public ResponseEntity<?> getChallengeProgress(@RequestHeader("token") String token) {
        try {
            Map<Challenge, Double> progress = challengeService.getChallengeProgress(token);  // Cambiado de authService a challengeService
			Map<ChallengeDTO, Double>progressdtos = new HashMap<>();
			for (Map.Entry<Challenge, Double> entry : progress.entrySet()) {
				progressdtos.put(challengeToDTO(entry.getKey()), entry.getValue());
			}
            return new ResponseEntity<>(progressdtos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

	ChallengeDTO challengeToDTO(Challenge challenge) {
		return new ChallengeDTO(challenge.getName(), challenge.getSport(), challenge.getTargetDistance(),
				challenge.getTargetTime(), challenge.getStartDate(), challenge.getEndDate());
	}
}
