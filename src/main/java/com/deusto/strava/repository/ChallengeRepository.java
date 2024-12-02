package com.deusto.strava.repository;

import com.deusto.strava.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
	Optional<Challenge>findById(Long id);
}
