package com.deusto.strava.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String email;
    private String name;
    private Date birthDate;
    private double weight;
    private double height;
    private int maxHeartRate;
    private int restHeartRate;

    // Lista de sesiones de entrenamiento
    private List<TrainingSession> trainingSessions = new ArrayList<>();

    // Lista de desafíos en los que participa
    private List<Challenge> challenges = new ArrayList<>();

    // Constructor
    public User() {
        this.email = "email";
        this.name = "name";
        this.birthDate = new Date();
        this.weight = 0.0;
        this.height = 0.0;
        this.maxHeartRate = 0;
        this.restHeartRate = 0;
        this.trainingSessions = new ArrayList<>();
        this.challenges = new ArrayList<>();
    }

    public User(String email, String name, double weight, double height, Date birthDate) {
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.weight = weight;
        this.height = height;
        this.maxHeartRate = 0;
        this.restHeartRate = 0;
        this.trainingSessions = new ArrayList<>();
        this.challenges = new ArrayList<>();
    }

    // Métodos
    public void addTrainingSession(TrainingSession session) {
        this.trainingSessions.add(session);
    }

    public void addChallenge(Challenge challenge) {
        this.challenges.add(challenge);
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public List<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }
}
