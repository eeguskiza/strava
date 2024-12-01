package com.deusto.strava;

import com.deusto.strava.dto.LoginRequestDTO;
import com.deusto.strava.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initData(AuthService authService) {
        return args -> {
            // Formateador para fechas
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Crear usuarios de ejemplo
            try {
                LoginRequestDTO user1 = new LoginRequestDTO(
                        "eeguskiza",
                        "Erik",
                        dateFormat.parse("2004-08-04"),
                        "password123",
                        90.0,
                        1.87,
                        180,
                        60,
                        null,
                        null
                );

                LoginRequestDTO user2 = new LoginRequestDTO(
                        "jane.smith@example.com",
                        "Jane Smith",
                        dateFormat.parse("1985-09-20"),
                        "password456",
                        65.0,
                        1.70,
                        170,
                        55,
                        null,
                        null
                );

                LoginRequestDTO user3 = new LoginRequestDTO(
                        "mike.jones@example.com",
                        "Mike Jones",
                        dateFormat.parse("1995-12-05"),
                        "password789",
                        85.0,
                        1.85,
                        175,
                        58,
                        null,
                        null
                );

                // Registrar usuarios en el sistema
                //authService.register(user1);
                //authService.register(user2);
                //authService.register(user3);

                logger.info("Users successfully registered in the system!");

            } catch (ParseException e) {
                logger.error("Error parsing dates for user initialization", e);
            }
        };
    }
}
