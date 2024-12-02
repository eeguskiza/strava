package com.deusto.strava.repository;

import com.deusto.strava.entity.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
	Optional<TrainingSession>findById(Long id);
}
