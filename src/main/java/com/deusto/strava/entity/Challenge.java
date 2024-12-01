package com.deusto.strava.entity;

import java.util.Date;

public class Challenge {
    private String id;
    public static int count = 0;
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

    public Challenge() {

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

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public void setTargetDistance(float targetDistance) {
        this.targetDistance = targetDistance;
    }

    public void setTargetTime(float targetTime) {
        this.targetTime = targetTime;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
