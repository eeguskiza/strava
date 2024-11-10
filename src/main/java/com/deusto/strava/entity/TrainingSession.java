package com.deusto.strava.entity;

import java.util.Date;


public class TrainingSession {
    private String title;
    private String sport;
    private float distance;
    private String startDate;
    private String startTime;
    private float duration;

    public TrainingSession() {

    }

    public TrainingSession(String title, String sport, float distance, String startDate, String startTime, float duration) {
        this.title = title;
        this.sport = sport;
        this.distance = distance;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}
