package com.deusto.strava;

import com.deusto.strava.entity.User;
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
                User user1 = new User("test", "Erik", 90.0f, 1.87f, dateFormat.parse("2004-08-04"));
                User user2 = new User("jane.smith@example.com", "Jane Smith", 65.0f, 1.70f, dateFormat.parse("1985-09-20"));
                User user3 = new User("mike.jones@example.com", "Mike Jones", 85.0f, 1.85f, dateFormat.parse("1995-12-05"));

                // Registrar usuarios en el sistema
                authService.register(user1);
                authService.register(user2);
                authService.register(user3);

                logger.info("Users successfully registered in the system!");

            } catch (ParseException e) {
                logger.error("Error parsing dates for user initialization", e);
            }
        };
    }
}
