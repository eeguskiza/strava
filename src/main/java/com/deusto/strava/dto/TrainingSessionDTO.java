package com.deusto.strava.dto;

import java.util.Date;

public class TrainingSessionDTO {
    private String id;
    private String sport;
    private float distance;
    private Date startDate;
    private float duration;

    // Constructor
    public TrainingSessionDTO(String id, String sport, float distance, Date startDate, float duration) {
        this.id = id;
        this.sport = sport;
        this.distance = distance;
        this.startDate = startDate;
        this.duration = duration;
    }

    // Getters y Setters
    public String getId() {
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
