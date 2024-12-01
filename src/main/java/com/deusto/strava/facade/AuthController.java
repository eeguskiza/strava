package com.deusto.strava.facade;

import com.deusto.strava.dto.CredentialsDTO;
import com.deusto.strava.dto.LoginRequestDTO;
import com.deusto.strava.dto.LoginRequestDTO;
import com.deusto.strava.entity.User;
import com.deusto.strava.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authorization Controller", description = "Endpoints for user registration, login, and logout")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register endpoint
    @Operation(
            summary = "Register a new user",
            description = "Allows a user to register by providing their details.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created: User successfully registered"),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Invalid user data"),
            }
    )

    @PostMapping("/user")
    public ResponseEntity<String> register(@RequestBody LoginRequestDTO userDTO) {
        if (authService.register(userDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
            //return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    // Login endpoint
    @Operation(
            summary = "Login a user",
            description = "Allows a user to log in by providing their email. Returns a token if successful.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Login successful, returns a token"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized: User not found"),
            }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody CredentialsDTO credentials) {
        Optional<String> token = authService.login(credentials.getEmail(), credentials.getPassword()).describeConstable();
        if (token.isPresent()) {
            return new ResponseEntity<>(token.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Logout endpoint
    @Operation(
            summary = "Logout a user",
            description = "Allows a user to log out by providing their authorization token.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content: Logout successful"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid token"),
            }
    )

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody String token) {
        if (authService.logout(token)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Logout successful");
            //return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or already logged out");
            //return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
