/**
 * This code is based on solutions provided by ChatGPT 4.0 and
 * adapted using GitHub Copilot. It has been thoroughly reviewed
 * and validated to ensure correctness and that it is free of errors.
 */
package com.deusto.strava.facade;

import com.deusto.strava.dto.UserDTO;
import com.deusto.strava.dto.LoginRequestDTO;
import com.deusto.strava.dto.LogoutRequestDTO;
import com.deusto.strava.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        String result = authService.register(userDTO);
        if ("User registered successfully.".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    // Login endpoint
    @Operation(
            summary = "Login to the system",
            description = "Allows a user to login by providing email and password. Returns a token if successful.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Login successful, returns a token"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials, login failed")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Parameter(name = "loginRequest", description = "User's login credentials", required = true)
            @RequestBody LoginRequestDTO loginRequest) {
        Optional<String> token = Optional.ofNullable(authService.login(loginRequest.getEmail(), loginRequest.getPassword()));

        return token
                .map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    // Logout endpoint
    @Operation(
            summary = "Logout from the system",
            description = "Allows a user to logout by providing the authorization token.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content: Logout successful"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid token, logout failed")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequestDTO logoutRequest) {
        boolean result = authService.logout(logoutRequest.getToken());

        if (result) {
            return new ResponseEntity<>("User logged out successfully.", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Invalid token or user not logged in.", HttpStatus.UNAUTHORIZED);
        }
    }
}
