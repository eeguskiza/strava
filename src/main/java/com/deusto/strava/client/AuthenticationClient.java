package com.deusto.strava.gateway;

import com.deusto.strava.dto.CredentialsDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Component
public class AuthenticationClient {

    private final RestTemplate restTemplate;
    private static final String GOOGLE_AUTH_URL = "http://localhost:9090/api/google/login";
    private static final String FACEBOOK_SERVER_HOST = "localhost";
    private static final int FACEBOOK_SERVER_PORT = 9091;

    public AuthenticationClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Authenticates with Google using REST.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticateWithGoogle(String email, String password) {
        try {
            var requestBody = new CredentialsDTO(email, password);
            var response = restTemplate.postForEntity(GOOGLE_AUTH_URL, requestBody, Boolean.class);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            System.err.println("Error authenticating with Google: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticates with Facebook using sockets.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticateWithFacebook(String email, String password) {
        try (Socket socket = new Socket(FACEBOOK_SERVER_HOST, FACEBOOK_SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send authentication request
            out.println("AUTHENTICATE");
            out.println(email);
            out.println(password);

            // Read the response
            String response = in.readLine();
            return "AUTH_SUCCESS".equalsIgnoreCase(response);
        } catch (Exception e) {
            System.err.println("Error authenticating with Facebook: " + e.getMessage());
            return false;
        }
    }
}
