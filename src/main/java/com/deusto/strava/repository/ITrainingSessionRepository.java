package com.deusto.strava.repository;

import com.deusto.strava.entity.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ITrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
	Optional<TrainingSession>findById(Long id);
}
