package com.deusto.strava.facade;

import com.deusto.strava.dto.LoginRequestDTO;
import com.deusto.strava.dto.UserDTO;
import com.deusto.strava.entity.User;
import com.deusto.strava.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authorization Controller", description = "Register, login, and logout operations")
public class AuthFacade {

    private final AuthService authService;

    @Autowired
    public AuthFacade(AuthService authService) {
        this.authService = authService;
    }

    // Register endpoint
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user by providing the necessary details.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created: User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Invalid data provided")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Parameter(name = "userDTO", description = "User registration details", required = true)
            @RequestBody UserDTO userDTO) {

            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getName());
            user.setBirthDate(userDTO.getBirthDate());
            user.setWeight(userDTO.getWeight());
            user.setHeight(userDTO.getHeight());
            user.setMaxHeartRate(userDTO.getMaxHeartRate());
            user.setRestHeartRate(userDTO.getRestHeartRate());

            String result = authService.register(user);

        if ("User registered successfully.".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequestDTO loginRequest) {
        String token = authService.login(loginRequest.getEmail());
        if ("User not found.".equals(token)) {
            return new ResponseEntity<>(token, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody String token) {
        // Verifica si el token es válido y está asociado a un usuario registrado
        String email = authService.getEmailByToken(token);
        if (email == null) {
            return new ResponseEntity<>("Invalid token or user not logged in.", HttpStatus.UNAUTHORIZED);
        }

        // Si el token es válido, proceder a eliminarlo
        boolean result = authService.logout(token);

        if (result) {
            return new ResponseEntity<>("User logged out successfully.", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Error occurred while logging out.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
