package com.deusto.strava.dto;

import java.util.Date;
import java.util.List;

public class UserDTO {
    private String email;
    private String name;
    private Date birthDate;
    private float weight;
    private float height;
    private int maxHeartRate;
    private int restHeartRate;
    private List<String> trainingSessionIds;
    private List<String> challengeIds;

    // Constructor vacío para la deserialización
    public UserDTO() {}

    // Constructor con todos los campos
    public UserDTO(String email, String name, Date birthDate, float weight, float height, int maxHeartRate, int restHeartRate, List<String> trainingSessionIds, List<String> challengeIds) {
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.weight = weight;
        this.height = height;
        this.maxHeartRate = maxHeartRate;
        this.restHeartRate = restHeartRate;
        this.trainingSessionIds = trainingSessionIds;
        this.challengeIds = challengeIds;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(int maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public int getRestHeartRate() {
        return restHeartRate;
    }

    public void setRestHeartRate(int restHeartRate) {
        this.restHeartRate = restHeartRate;
    }

    public List<String> getTrainingSessionIds() {
        return trainingSessionIds;
    }

    public void setTrainingSessionIds(List<String> trainingSessionIds) {
        this.trainingSessionIds = trainingSessionIds;
    }

    public List<String> getChallengeIds() {
        return challengeIds;
    }

    public void setChallengeIds(List<String> challengeIds) {
        this.challengeIds = challengeIds;
    }
}
