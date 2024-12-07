package com.deusto.strava.service;

import java.util.HashMap;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.deusto.strava.dto.CredentialsDTO;
import com.deusto.strava.dto.UserDTO;
import com.deusto.strava.entity.User;
import com.deusto.strava.repository.IUserRepository;

import factory.FactoryUser;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    // User repository interface
    private final IUserRepository userRepository;

    // In-memory token storage
    // Instead of saving tokens to a database, we store them in a HashMap.
    // Key: Token (String), Value: User (User)
    private HashMap<String, User> tokenRepository = new HashMap<>();

    /**
     * Constructor that receives an IUserRepository implementation.
     *
     * @param userRepository the repository for user entities.
     */
    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Converts a LoginRequestDTO object to a User entity.
     *
     * @param userDTO the DTO containing user data.
     * @return the User entity created from the DTO.
     * @throws IllegalArgumentException if the birth date is null.
     */
    private User dtoToUser(UserDTO userDTO) {
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
     * Registers a new user if the email is unique and the credentials are valid.
     *
     * @param userDTO the DTO containing the user's registration data.
     * @return true if the user is successfully registered, false otherwise.
     * @throws IllegalArgumentException if the credentials are invalid.
     */
    public boolean register(UserDTO userDTO) {
        User user = dtoToUser(userDTO);

        // Validate credentials based on the selected service
        boolean isValid = FactoryUser.createService(userDTO.getService())
                .authenticate(userDTO.getEmail(), userDTO.getPassword());

        if (isValid) {
            // Check if the user does not already exist
            if (getUserByEmail(userDTO.getEmail()) == null) {
                addUser(user);
                logger.info("User registered successfully with {}: {}", userDTO.getService(), userDTO.getEmail());
                return true;
            }
        } else {
            logger.warn("Invalid credentials on {} for email: {}", userDTO.getService(), user.getEmail());
            throw new IllegalArgumentException("Invalid credentials on " + userDTO.getService() + ". Registration denied.");
        }

        logger.warn("User already exists: {}", user.getEmail());
        return false;
    }

    /**
     * Logs in a user by their email and generates a session token.
     *
     * @param credentials the DTO containing the user's login credentials.
     * @return a token if the login is successful, otherwise throws an exception.
     * @throws IllegalArgumentException if credentials are invalid or the user is not found.
     */
    public String login(CredentialsDTO credentials) {
        // Validate that none of the required fields are null or empty
        if (credentials.getEmail() == null || credentials.getEmail().isEmpty() ||
                credentials.getPassword() == null || credentials.getPassword().isEmpty() ||
                credentials.getService() == null || credentials.getService().isEmpty()) {
            throw new IllegalArgumentException("Email, password, or service cannot be null or empty");
        }

        // Validate the credentials through the selected external service (e.g., Google, Facebook)
        boolean isValid = FactoryUser.createService(credentials.getService())
                .authenticate(credentials.getEmail(), credentials.getPassword());

        if (!isValid) {
            throw new IllegalArgumentException("Invalid credentials for " + credentials.getService() + ". Login denied.");
        }

        // Look up the user in the local repository
        User user = getUserByEmail(credentials.getEmail());

        // Check if the user exists locally
        if (user == null) {
            throw new IllegalArgumentException("User not found for email: " + credentials.getEmail());
        }

        // Generate a unique token for the session
        String token = generateToken();
        tokenRepository.put(token, user);

        // Log the successful login
        logger.info("User logged in successfully with {}: {}", credentials.getService(), credentials.getEmail());

        return token;
    }

    /**
     * Logs out a user by removing their token from the in-memory repository.
     *
     * @param token the session token of the user.
     * @return true if logout is successful, false otherwise.
     */
    public boolean logout(String token) {
        if (tokenRepository.containsKey(token)) {
            tokenRepository.remove(token);
            logger.info("User logged out successfully with token: {}", token);
            return true;
        }
        logger.warn("User not found for token: {}", token);
        return false;
    }

    /**
     * Adds a user to the repository if they do not already exist.
     *
     * @param user the user entity to be added.
     */
    public void addUser(User user) {
        if (user != null) {
            userRepository.save(user);
        }
    }

    /**
     * Retrieves a user based on their session token.
     *
     * @param token the session token of the user.
     * @return the User entity associated with the token or null if not found.
     */
    public User getUserByToken(String token) {
        return tokenRepository.get(token);
    }

    /**
     * Retrieves a user based on their email address.
     *
     * @param email the email address of the user.
     * @return the User entity associated with the email, or null if not found.
     */
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    /**
     * Synchronized method to generate a unique session token.
     * Uses current time in milliseconds to create a hex string.
     *
     * @return a unique token as a hexadecimal string.
     */
    private static synchronized String generateToken() {
        return Long.toHexString(System.currentTimeMillis());
    }
}
