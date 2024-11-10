package com.deusto.strava.facade;

import com.deusto.strava.dto.LoginRequestDTO;
import com.deusto.strava.dto.UserDTO;
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
        String result = authService.register(userDTO);
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

    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody String token) {
        boolean result = authService.logout(token);
        if (result) {
            return new ResponseEntity<>("User logged out successfully.", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
        }
    }
}
