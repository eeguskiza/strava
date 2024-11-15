package com.deusto.strava.entity;

import java.util.Date;

public class Challenge {
    private String id;
    private static int count = 0;
    private String creatorName;
    private String name;
    private String sport;
    private float targetDistance;
    private float targetTime;
    private Date startDate;
    private Date endDate;

    // Constructor
    public Challenge(String creatorName, String name, String sport, float targetDistance, float targetTime, Date startDate, Date endDate) {
        this.id = "challenge_" + (++count);
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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
