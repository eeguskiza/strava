package com.deusto.strava.entity;

public class Challenge {
    private String name;
    private String sport;
    private double targetDistance;
    private double targetTime;
    private String startDate;
    private String endDate;

    public Challenge() {
    }

    public Challenge(String name, String sport, double targetDistance, double targetTime, String startDate, String endDate) {
        this.name = name;
        this.sport = sport;
        this.targetDistance = targetDistance;
        this.targetTime = targetTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public double getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(double targetTime) {
        this.targetTime = targetTime;
    }

    public double getTargetDistance() {
        return targetDistance;
    }

    public void setTargetDistance(double targetDistance) {
        this.targetDistance = targetDistance;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
