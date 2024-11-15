package com.deusto.strava.entity;

import java.util.Date;

public class TrainingSession {
    private String id;
    private static int count = 0;
    private String sport;
    private float distance;
    private Date startDate;
    private float duration;

    // Constructor
    public TrainingSession(String sport, float distance, Date startDate, float duration) {
        this.id = "session_" + (++count);
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
