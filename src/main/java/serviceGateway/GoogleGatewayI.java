package serviceGateway;

import com.deusto.strava.dto.LoginDTO;
import org.springframework.web.client.RestTemplate;

import com.deusto.strava.dto.CredentialsDTO;

public class GoogleGatewayI implements IServiceGateway {
	private final RestTemplate restTemplate = new RestTemplate();
	private static final String GOOGLE_AUTH_URL = "http://localhost:9090/api/google/login";
	public boolean authenticate(String email, String password) {
		try {
            var requestBody = new LoginDTO(email, password);
            var response = restTemplate.postForEntity(GOOGLE_AUTH_URL, requestBody, Boolean.class);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            System.err.println("Error authenticating with Google: " + e.getMessage());
            return false;
        }
	}
}
