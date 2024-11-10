package com.deusto.strava.facade;

import com.deusto.strava.dto.UserDTO;
import com.deusto.strava.dto.LoginRequestDTO;
import com.deusto.strava.dto.LogoutRequestDTO;
import com.deusto.strava.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthFacade {

    private final AuthService authService;

    @Autowired
    public AuthFacade(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserDTO userDTO) {
        return authService.register(userDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO loginRequest) {
        return authService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/logout")
    public String logout(@RequestBody LogoutRequestDTO logoutRequest) {
        return authService.logout(logoutRequest.getToken());
    }
}
