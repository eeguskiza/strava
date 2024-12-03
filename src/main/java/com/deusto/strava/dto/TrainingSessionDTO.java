package com.deusto.strava.dto;

import java.sql.Date;

public class TrainingSessionDTO {
    private Long id;
    private String sport;
    private float distance;
    private Date startDate;
    private float duration;

    // Constructor
	public TrainingSessionDTO() {
	}
    public TrainingSessionDTO(Long id, String sport, float distance, Date startDate, float duration) {
        this.id = id;
        this.sport = sport;
        this.distance = distance;
        this.startDate = startDate;
        this.duration = duration;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getSport() {
        return sport;
    }

    public float getDistance() {
        return distance;
    }

    public Date getStartDate() {
        return startDate;
    }

    public float getDuration() {
        return duration;
    }
}
