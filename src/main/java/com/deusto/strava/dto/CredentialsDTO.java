package com.deusto.strava.dto;

public class CredentialsDTO {
    private String email;
    private String password;
    private String service; // Nuevo campo para elegir el servicio de autenticación

    // Constructor vacío para la deserialización
    public CredentialsDTO() {}

    // Constructor con los campos necesarios
    public CredentialsDTO(String email, String password, String service) {
        this.email = email;
        this.password = password;
        this.service = service;
    }

    public CredentialsDTO(String email, String password) {
        this.email = email;
        this.password = password;
        this.service = null; // O puedes definir un valor por defecto si es necesario
    }


    // Getters y Setters
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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
