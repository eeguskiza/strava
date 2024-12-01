package com.deusto.strava.gateway;

import com.deusto.strava.dto.CredentialsDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Component
public class GoogleGatewayImpl implements GoogleGateway {

    private final RestTemplate restTemplate;
    private static final String GOOGLE_AUTH_URL = "http://localhost:9090/api/google/login";

    public GoogleGatewayImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public boolean verifyCredentials(String email, String password) {
        try {
            // Crear el cuerpo de la petición
            var requestBody = new CredentialsDTO(email, password);

            // Hacer la llamada al servidor de Google
            ResponseEntity<Boolean> response = restTemplate.postForEntity(GOOGLE_AUTH_URL, requestBody, Boolean.class);

            // Comprobar si la respuesta es exitosa
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            // Log y manejo de errores
            System.err.println("Error al verificar credenciales con Google: " + e.getMessage());
            return false;
        }
    }

}
