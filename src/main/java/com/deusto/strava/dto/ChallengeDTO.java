package com.deusto.strava.dto;

import java.util.Date;

public class ChallengeDTO {
    private String id;
    private String creatorName;
    private String name;
    private String sport;
    private float targetDistance;
    private float targetTime;
    private Date startDate;
    private Date endDate;

    // Constructor
    public ChallengeDTO(String id, String creatorName, String name, String sport, float targetDistance, float targetTime, Date startDate, Date endDate) {
        this.id = id;
        this.creatorName = creatorName;
        this.name = name;
        this.sport = sport;
        this.targetDistance = targetDistance;
        this.targetTime = targetTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getName() {
        return name;
    }

    public String getSport() {
        return sport;
    }

    public float getTargetDistance() {
        return targetDistance;
    }

    public float getTargetTime() {
        return targetTime;
    }

    public java.sql.Date getStartDate() {
        return (java.sql.Date) startDate;
    }

    public java.sql.Date getEndDate() {
        return (java.sql.Date) endDate;
    }
}
