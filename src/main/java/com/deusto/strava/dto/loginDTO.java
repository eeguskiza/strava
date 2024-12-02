package com.deusto.strava.dto;

public class loginDTO {
    private String email;
    private String password;

    // Constructor vacío para la deserialización
    public loginDTO() {}

    // Constructor con el campo `email`
    public loginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter y Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}