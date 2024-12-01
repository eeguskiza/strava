package com.deusto.strava.service;

import com.deusto.strava.dto.LoginRequestDTO;
import com.deusto.strava.entity.User;
import com.deusto.strava.gateway.GoogleGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    // Simulated user repository to store registered users
    private final Map<String, User> userRepository = new HashMap<>();

    // Simulated token store to manage active user sessions
    private final Map<String, User> tokenStore = new HashMap<>();

    private final GoogleGateway googleAuthGateway;

    public AuthService(GoogleGateway googleAuthGateway) {
        this.googleAuthGateway = googleAuthGateway;
    }

    /**
     * Converts a UserDTO object to a User entity.
     *
     * @param userDTO the DTO containing user data.
     * @return the User entity created from the DTO.
     */
    private User dtoToUser(LoginRequestDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setWeight(userDTO.getWeight());
        user.setHeight(userDTO.getHeight());
        if (userDTO.getBirthDate() != null) {
            user.setBirthDate(new java.sql.Date(userDTO.getBirthDate().getTime()));
        } else {
            throw new IllegalArgumentException("Birthdate cannot be null");
        }

        return user;
    }


    /**
     * Registers a new user if the email is unique.
     *
     * @param userDTO the DTO containing the user's registration data.
     * @return true if the user is successfully registered, false otherwise.
     */
    public boolean register(LoginRequestDTO userDTO) {
        // Convert the DTO into a User entity
        User user = dtoToUser(userDTO);

        // Validate credentials with Google before registering the user
        boolean isValid = googleAuthGateway.verifyCredentials(user.getEmail(), userDTO.getPassword());
        if (!isValid) {
            logger.warn("Invalid credentials on Google for email: {}", user.getEmail());
            throw new IllegalArgumentException("Invalid credentials on Google. Registration denied.");
        }

        // Register the user if the credentials are valid and the email is unique
        if (user != null && user.getEmail() != null && !userRepository.containsKey(user.getEmail())) {
            userRepository.put(user.getEmail(), user);
            logger.info("User successfully registered: {}", user.getEmail());
            return true;
        }

        logger.warn("User already exists: {}", user.getEmail());
        return false;
    }



    /**
     * Logs in a user by their email and generates a session token.
     *
     * @param email the email of the user attempting to log in.
     * @return a token if the login is successful, null otherwise.
     */
    public String login(String email, String password) {
        User user = userRepository.get(email);
        if (user != null) {
            String token = generateToken();  // Generate a random token for the session
            tokenStore.put(token, user);     // Store the token and associate it with the user
            return token;
        }
        return null;
    }

    /**
     * Logs out a user by removing their token from the store.
     *
     * @param token the session token of the user.
     * @return true if the logout is successful, false otherwise.
     */
    public boolean logout(String token) {
        if (tokenStore.containsKey(token)) {
            tokenStore.remove(token);
            return true;
        }
        return false;
    }

    /**
     * Adds a user to the repository if they do not already exist.
     *
     * @param user the user entity to be added.
     */
    public void addUser(User user) {
        if (user != null) {
            userRepository.putIfAbsent(user.getEmail(), user);
        }
    }

    /**
     * Retrieves a user based on their session token.
     *
     * @param token the session token of the user.
     * @return the User entity associated with the token.
     */
    public User getUserByToken(String token) {
        return tokenStore.get(token);
    }

    /**
     * Retrieves a user based on their email address.
     *
     * @param email the email address of the user.
     * @return the User entity associated with the email.
     */
    public User getUserByEmail(String email) {
        return userRepository.get(email);
    }

    /**
     * Synchronized method to generate a unique session token.
     *
     * @return a unique token as a hexadecimal string.
     */
    private static synchronized String generateToken() {
        return Long.toHexString(System.currentTimeMillis());
    }
}
