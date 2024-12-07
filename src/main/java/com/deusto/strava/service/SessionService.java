package com.deusto.strava.service;

import com.deusto.strava.entity.TrainingSession;
import com.deusto.strava.entity.User;
import com.deusto.strava.repository.ITrainingSessionRepository;
import com.deusto.strava.repository.IUserRepository;
import com.deusto.strava.dto.TrainingSessionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Autowired
    private AuthService authService;
    private final IUserRepository IUserRepository;
    private final ITrainingSessionRepository ITrainingSessionRepository;

	public SessionService(IUserRepository IUserRepository, ITrainingSessionRepository ITrainingSessionRepository) {
		this.IUserRepository = IUserRepository;
		this.ITrainingSessionRepository = ITrainingSessionRepository;
	}

    public String createSession(String token, TrainingSessionDTO sessionDTO) {
        // Verificar el token
        User user = authService.getUserByToken(token);
        if (user == null) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        // Crear una nueva sesión de entrenamiento
        TrainingSession session = new TrainingSession(
                sessionDTO.getSport(),
                sessionDTO.getDistance(),
                sessionDTO.getStartDate(),
                sessionDTO.getDuration()
        );

        // Establecer la relación entre el usuario y la sesión
        session.setUser(user);

        // Añadir la sesión a la lista de sesiones del usuario
        user.getTrainingSessions().add(session);

        // Guardar los cambios y obtener la sesión persistida
        IUserRepository.save(user);

        // Buscar la sesión persistida en la lista del usuario (opcional si no hay cascade)
        TrainingSession savedSession = user.getTrainingSessions()
                .stream()
                .filter(ts -> ts.getStartDate().equals(sessionDTO.getStartDate()) &&
                        ts.getDistance() == sessionDTO.getDistance())
                .findFirst()
                .orElse(session); // Esto debería ser suficiente en la mayoría de los casos.

        // Retornar el ID generado
        return "Training session created successfully with ID: " + savedSession.getId();
    }




    public List<TrainingSession> getSessions(String token, Date startDate, Date endDate) {
        User user = authService.getUserByToken(token);
        if (user==null) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        if (startDate != null && endDate != null) {
            return user.getTrainingSessions().stream()
                    .filter(session -> !session.getStartDate().before(startDate) &&
                            !session.getStartDate().after(endDate))
                    .collect(Collectors.toList());
        }

        return user.getTrainingSessions();
    }
}
