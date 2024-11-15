package com.deusto.strava.dto;

public class CredentialsDTO {
    private String email;

    // Constructor vacío para la deserialización
    public CredentialsDTO() {}

    // Constructor con el campo `email`
    public CredentialsDTO(String email) {
        this.email = email;
    }

    // Getter y Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
