package com.deusto.strava.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.deusto.strava.dto.CredentialsDTO;
import com.deusto.strava.dto.LoginRequestDTO;
import com.deusto.strava.entity.Token;
import com.deusto.strava.entity.User;
import com.deusto.strava.repository.TokenRepository;
import com.deusto.strava.repository.UserRepository;

import factory.FactoryUser;
import serviceGateway.ServiceGateway;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

 

    // Google or Facebook authentication gateway
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public AuthService(UserRepository userRepository, TokenRepository tokenRepository) {
    	this.userRepository = userRepository;
    	this.tokenRepository = tokenRepository;
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
        User user = dtoToUser(userDTO);

        // Validar credenciales en función del servicio seleccionado
        ServiceGateway serviceGateway = FactoryUser.selectService(userDTO.getService());
		if (serviceGateway == null) {
			throw new IllegalArgumentException("Invalid authentication service selected: " + userDTO.getService());
		}
		boolean isValid = false; 
		if(user != null && user.getEmail() != null) {
			isValid = serviceGateway.authenticate(userDTO.getEmail(), userDTO.getPassword());
		}
        if (isValid) {
        	if (getUserByEmail(userDTO.getEmail())==null){
                addUser(user);
                logger.info("User registered successfully with {}: {}", userDTO.getService(), userDTO.getEmail());
                return true;
        	}
        }else {
        	logger.warn("Invalid credentials on {} for email: {}", userDTO.getService(), user.getEmail());
            throw new IllegalArgumentException("Invalid credentials on " + userDTO.getService() + ". Registration denied.");
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
    public String login(CredentialsDTO credentials) {
        // Validar que los campos no sean nulos o vacíos
        if (credentials.getEmail() == null || credentials.getEmail().isEmpty() ||
                credentials.getPassword() == null || credentials.getPassword().isEmpty() ||
                credentials.getService() == null || credentials.getService().isEmpty()) {
            throw new IllegalArgumentException("Email, password, or service cannot be null or empty");
        }

        ServiceGateway serviceGateway = FactoryUser.selectService(credentials.getService());
		if (serviceGateway == null) {
			throw new IllegalArgumentException("Invalid authentication service selected: " + credentials.getService());
		}
		boolean isValid = serviceGateway.authenticate(credentials.getEmail(), credentials.getPassword());

        if (!isValid) {
            throw new IllegalArgumentException("Invalid credentials for " + credentials.getService() + ". Login denied.");
        }

        // Buscar el usuario en el repositorio interno
        User user = getUserByEmail(credentials.getEmail());

        // Validar si el usuario existe localmente
        if (user == null) {
            throw new IllegalArgumentException("User not found for email: " + credentials.getEmail());
        }

        // Generar un token único para la sesión
        String token = generateToken();
        tokenRepository.save(new Token(token, user));

        // Registrar el éxito del inicio de sesión
        logger.info("User logged in successfully with {}: {}", credentials.getService(), credentials.getEmail());

        return token;
    }



    /**
     * Logs out a user by removing their token from the store.
     *
     * @param token the session token of the user.
     * @return true if the logout is successful, false otherwise.
     */
    public boolean logout(String token) {
		if (token != null && tokenRepository.findByToken(token).isPresent()) {
			tokenRepository.deleteByToken(token);
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
            userRepository.save(user);
        }
    }

    /**
     * Retrieves a user based on their session token.
     *
     * @param token the session token of the user.
     * @return the User entity associated with the token.
     */
    public User getUserByToken(String token) {
    	Optional<Token>t = tokenRepository.findByToken(token);
		if (t.isPresent()) {
			return t.get().getUser();
		}
		return null;
    }

    /**
     * Retrieves a user based on their email address.
     *
     * @param email the email address of the user.
     * @return the User entity associated with the email.
     */
    public User getUserByEmail(String email) {
    	Optional<User> user = userRepository.findByEmail(email);
    	if (user.isPresent()) {
    		return user.get();
    	}
    	return null;	
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
